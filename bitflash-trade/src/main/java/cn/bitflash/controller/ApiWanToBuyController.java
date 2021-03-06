package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.UserAccount;
import cn.bitflash.loginutil.LoginUtils;
import cn.bitflash.service.*;
import cn.bitflash.sysutil.SysUtils;
import cn.bitflash.trade.*;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.usertradeutil.UserUtils;
import cn.bitflash.utils.Common;
import cn.bitflash.utils.R;
import cn.bitflash.utils.RedisUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.exception.RRException;
import common.utils.GeTuiSendMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import static cn.bitflash.utils.Common.*;

/**
 * 求购
 *
 * @author gaoyuguo
 * @date 2018-8-28 15:22:06
 * 求购状态：
 * 求购者：‘1’：可撤销； ‘2’：待付款； ‘4’：待收币   ‘6’完成  ‘0’已撤销   ‘9’申诉
 * 卖出者：    -     ； ‘3’：待收款； ‘5’：待确认
 */
@RestController
@RequestMapping("/api/need")
public class ApiWanToBuyController {

    @Autowired
    private UserBuyService userBuyService;

    @Autowired
    private UserBuyHistoryService userBuyHistoryService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserTradeConfigService userTradeConfigService;

    @Autowired
    private UserBrokerageService userBrokerageService;

    @Autowired
    private UserComplaintService userComplaintService;

    @Autowired
    private TradePoundageService tradePoundageService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private LoginUtils loginUtils;

    @Autowired
    private SysUtils sysUtils;

    @Autowired
    private RedisUtils redisUtils;


    /**-----------------------------------------------显示求购信息列表-----------------------------------------------------*/

    /**
     * ----------------交易页-----------------
     *
     * @param pages 分页
     * @return 除用户所有求购信息
     */
    @Login
    @PostMapping("showBuyMessage")
    public R showNeedMessage(@RequestAttribute("uid") String uid, @RequestParam("pages") String pages, @UserAccount UserAccountEntity userAccount) {
        List<UserBuyMessageBean> ub = userBuyService.getBuyMessage(uid, Integer.valueOf(pages));
        if (ub == null || ub.size() < 0) {
            return R.error("暂时没有求购信息");
        }
        Integer count = userBuyService.getNumToPaging();
        return R.ok().put("count", count).put("list", ub).put("availableAssets", userAccount.getAvailableAssets());
    }

    /**
     * ---------------订单页----------------
     *
     * @param pages 分页
     * @return 用户的所有交易信息
     */
    @Login
    @PostMapping("showBuyMessageOwn")
    public R showUserBuyMessage(@RequestAttribute("uid") String uid, @RequestParam("pages") String pages) {
        List<UserBuyBean> userBuyEntities = userBuyService.selectBuyList(uid, Integer.valueOf(pages));
        List<UserBuyBean> userBuyEntitiesList = new LinkedList<UserBuyBean>();
        String state = null;

        Integer count = userBuyService.selectUserBuyOwnCount(uid);

        for (UserBuyBean userBuyEntity : userBuyEntities) {
            if (userBuyEntity.getUid().equals(uid)) {
                state = userBuyEntity.getState();
            } else if (uid.equals(userBuyEntity.getSellUid())) {
                state = userBuyEntity.getSellState();
            } else if (uid.equals(userBuyEntity.getPurchaseUid())) {
                state = userBuyEntity.getPurchaseState();
            }

            if (STATE_BUY_CANCEL.equals(state)) {
                state = "可撤销";
            } else if (STATE_BUY_ACCMONEY.equals(state)) {
                state = "待收款";
            } else if (STATE_BUY_PAYMONEY.equals(state)) {
                state = "待付款";
            } else if (STATE_BUY_PAYCOIN.equals(state)) {
                state = "待确认";
            } else if (STATE_BUY_ACCCOIN.equals(state)) {
                state = "待收币";
            }
            userBuyEntity.setState(state);
            userBuyEntitiesList.add(userBuyEntity);
        }


        return R.ok().put("userBuyEntitiesList", userBuyEntitiesList).put("count", count);
    }

    /**--------------------------------------------------添加订单---------------------------------------------------------*/

    /**
     * 添加求购信息
     *
     * @param userBuyEntity 订单信息
     * @return 交易状态
     */
    @Login
    @PostMapping("addBuyMessage")
    public R addBuyMessage(@RequestBody UserBuyEntity userBuyEntity, @RequestAttribute("uid") String uid) {
        if (userBuyEntity == null) {
            return R.error(501, "求购信息为空");
        }
        Float num = userBuyEntity.getQuantity();
        if (num % 100 != 0 || num < 100) {
            return R.error(502, "求购数量最低为100，且为100的倍数");
        }
        userBuyService.addBuyMessage(userBuyEntity, uid);
        return R.ok().put("code", SUCCESS);
    }

    /**-----------------------------------------------------下单---------------------------------------------------------*/

    /**
     * 下单
     *
     * @param id 订单id
     * @return 交易状态
     * <p>
     * 1.查询手续费，并从卖出者账号中扣除。如资金不足抛出错误
     * 2.添加手续费到trade_poundage
     * 3.修改user_buy中state为‘1’交易中状态
     * 4.添加订单到user_buy_history
     * 5.修改求购者
     */
    @Login
    @PostMapping("addBuyMessageHistory")
    @Transactional(propagation = Propagation.REQUIRED)
    public R addBuyMessageHistory(@RequestParam("id") String id, @RequestAttribute("uid") String uid) {

        //交易状态:'1'资金不足,'2'订单不存在

        UserBuyEntity userBuy = userBuyService.selectOne(new EntityWrapper<UserBuyEntity>().eq("id", id));
        if (userBuy == null || !STATE_BUY_CANCEL.equals(userBuy.getState())) {
            return R.ok().put("code", TRADEMISS);
        }

        //获取手续费
        Map<String, Float> map = poundage(id);
        //扣除手续费
        UserAccountEntity userAccountEntity = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", uid));
        //不足支付扣款
        if (new BigDecimal(map.get("totalQuantity")).compareTo(userAccountEntity.getAvailableAssets()) == 1) {
            return R.ok().put("code", FAIL);
        }
        deduct(new BigDecimal(map.get("totalQuantity")), uid);

        //添加手续费记录
        TradePoundageEntity tradePoundageEntity = new TradePoundageEntity();
        tradePoundageEntity.setCreateTime(new Date());
        tradePoundageEntity.setPoundage(new BigDecimal(map.get("totalPoundage")));
        tradePoundageEntity.setUid(uid);
        tradePoundageEntity.setUserTradeId(id);
        tradePoundageEntity.setQuantity(new BigDecimal(map.get("buyQuantity")));
        tradePoundageService.insert(tradePoundageEntity);

        //修改user_buy订单状态
        userBuy.setState(STATE_BUY_PAYMONEY);
        userBuyService.update(userBuy, new EntityWrapper<UserBuyEntity>().eq("id", id));

        //添加订单到user_buy_history
        UserBuyHistoryEntity buyHistory = new UserBuyHistoryEntity();
        buyHistory.setPrice(new BigDecimal(userBuy.getPrice()));
        buyHistory.setPurchaseState(STATE_BUY_PAYMONEY);
        buyHistory.setPurchaseUid(userBuy.getUid());
        buyHistory.setQuantity(new BigDecimal(userBuy.getQuantity()));
        buyHistory.setSellState(STATE_BUY_ACCMONEY);
        buyHistory.setSellUid(uid);
        buyHistory.setUserBuyId(id);
        userBuyHistoryService.insert(buyHistory);

        return R.ok().put("code", SUCCESS);
    }

    /**-------------------------------------------查看交易页求购详情页-----------------------------------------------------*/

    /**
     * 查看交易详情
     *
     * @param id 订单id
     * @return 订单详情
     */
    @PostMapping("showBuyMessagePage")
    public R showBuyMessagePage(@RequestParam("id") String id) {
        //订单详情
        UserBuyEntity userBuy = userBuyService.selectOne(new EntityWrapper<UserBuyEntity>().eq("id", id));
        //判定订单不存在
        if (userBuy == null || !"1".equals(userBuy.getState())) {
            return R.ok().put("code", TRADEMISS);
        }
        //获取手续费
        Map<String, Float> map = poundage(id);
        return R.ok().put("code", SUCCESS).put("userBuy", userBuy).put("poundage", map.get("poundage") * 100).put("totalMoney", map.get("totalMoney")).put("totalQuantity", map.get("totalQuantity"));
    }

    /**-------------------------------------------查看订单页求购详情页-----------------------------------------------------*/

    /**
     * 订单页
     *
     * @param id 订单id
     * @return 订单详情
     */
    @PostMapping("checkOrder")
    public R checkOrder(@RequestParam("id") String id) {
        Object userBean = new Object();

        UserBuyHistoryBean userBuyHistoryBean = userBuyHistoryService.selectBuyHistory(id);
        if (userBuyHistoryBean == null || "".equals(userBuyHistoryBean)) {
            UserBuyEntity userBuy = userBuyService.selectOne(new EntityWrapper<UserBuyEntity>().eq("id", id));
            if (userBuy == null) {
                return R.ok().put("code", "订单不存在");
            }
            userBean = userBuy;
        } else {
            userBean = userBuyHistoryBean;
        }
        Map<String, Float> map = this.poundage(id);

        return R.ok().put("userBean", userBean).put("totalQuantity", map.get("totalQuantity")).put("price", map.get("price")).put("buyQuantity", map.get("buyQuantity")).put("totalMoney", map.get("totalMoney"));
    }

    /**-------------------------------------------------点击操作----------------------------------------------------------*/

    /**
     * -----------------1-----------------
     * <p>
     * --------------点击撤销--------------
     */
    @PostMapping("cancel")
    @Transactional(propagation = Propagation.REQUIRED)
    public R cancelBuyMessage(@RequestParam String id) {
        UserBuyEntity ub = userBuyService.selectById(id);
        if (ub == null || STATE_BUY_CANCELFIISH.equals(ub.getState())) {
            return R.ok().put("code", FAIL);
        }
        if (ub.getState().equals(STATE_BUY_CANCEL)) {
            ub.setState(STATE_BUY_CANCELFIISH);
            ub.setCancelTime(new Date());
            userBuyService.updateById(ub);
            return R.ok().put("code", SUCCESS);
        }
        return R.ok();
    }

    /**
     * ---------------2-------------------
     * <p>
     * --------点击已付款(待付款)-----------
     */
    @PostMapping("payMoney")
    @Transactional(propagation = Propagation.REQUIRED)
    public R payMoney(@RequestParam("id") String id) {
        UserBuyEntity userBuyEntity = userBuyService.selectById(id);
            //设置支付时间
            userBuyEntity.setPayTime(new Date());
            userBuyService.update(userBuyEntity, new EntityWrapper<UserBuyEntity>().eq("id", id));
            //修改user_buy订单状态
            UserBuyEntity userBuy = userBuyService.selectById(id);
            userBuy.setState(STATE_BUY_ACCCOIN);
            userBuyService.update(userBuy, new EntityWrapper<UserBuyEntity>().eq("id", id));
            //修改交易状态
            UserBuyHistoryEntity userBuyHistoryEntity = userBuyHistoryService.selectOne(new EntityWrapper<UserBuyHistoryEntity>().eq("user_buy_id", id));
            userBuyHistoryEntity.setSellState(STATE_BUY_PAYCOIN);
            userBuyHistoryEntity.setPurchaseState(STATE_BUY_ACCCOIN);
            userBuyHistoryService.update(userBuyHistoryEntity, new EntityWrapper<UserBuyHistoryEntity>().eq("user_buy_id", id));
        return R.ok().put("code", SUCCESS);
    }

    /**
     * ------------------3--------------------
     * <p>
     * -------------点击取消(待付款)------------
     */
    @PostMapping("recall")
    @Transactional(propagation = Propagation.REQUIRED)
    public R recall(@RequestParam("id") String id) {
        //查询uid
        UserBuyHistoryEntity userBuyHistoryEntity = userBuyHistoryService.selectOne(new EntityWrapper<UserBuyHistoryEntity>().eq("user_buy_id", id));
        //获取trade_poundage手续费，并返还，删除该信息
        TradePoundageEntity tradePoundageEntity = tradePoundageService.selectOne(new EntityWrapper<TradePoundageEntity>().eq("user_trade_id", id));
        UserAccountEntity userAccountEntity = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", userBuyHistoryEntity.getSellUid()));
        userAccountEntity.setRegulateIncome(tradePoundageEntity.getPoundage().add(tradePoundageEntity.getQuantity()).add(userAccountEntity.getRegulateIncome()));
        userAccountEntity.setAvailableAssets(tradePoundageEntity.getPoundage().add(tradePoundageEntity.getQuantity()).add(userAccountEntity.getAvailableAssets()));
        userAccountService.update(userAccountEntity, new EntityWrapper<UserAccountEntity>().eq("uid", userBuyHistoryEntity.getSellUid()));
        tradePoundageService.delete(new EntityWrapper<TradePoundageEntity>().eq("user_trade_id", id));
        //删除求购历史订单
        userBuyHistoryService.delete(new EntityWrapper<UserBuyHistoryEntity>().eq("user_buy_id", id));
        userBuyService.delete(new EntityWrapper<UserBuyEntity>().eq("id", id));
        return R.ok().put("code", SUCCESS);
    }


    /**
     * ------------------4--------------------
     * <p>
     * -------------点击催单(待收款/待收币)------------
     */
    @PostMapping("reminders")
    @Transactional(propagation = Propagation.REQUIRED)
    public R reminders(@RequestParam("id") String id) {
        UserBuyHistoryEntity userBuyHistoryEntity = userBuyHistoryService.selectOne(new EntityWrapper<UserBuyHistoryEntity>().eq("user_buy_id", id));
        //获取Cid
        String cid = null;
        //获取推送信息
        String text = null;
        if (STATE_BUY_PAYMONEY.equals(userBuyHistoryEntity.getPurchaseState())) {
            cid = loginUtils.selectGT(new ModelMap("uid", userBuyHistoryEntity.getPurchaseUid())).getCid();
            text = sysUtils.getVal("paymoney");
        }
        if (STATE_BUY_ACCCOIN.equals(userBuyHistoryEntity.getPurchaseState())) {
            cid = loginUtils.selectGT(new ModelMap("uid", userBuyHistoryEntity.getSellUid())).getCid();
            text = sysUtils.getVal("reminders");
        }
        String idVal = redisUtils.get(Common.ADD_LOCKNUM + id);
        if (StringUtils.isBlank(idVal)) {
            try {
                GeTuiSendMessage.sendSingleMessage(text, cid);
                redisUtils.set(Common.ADD_LOCKNUM + id, id, 60 * 60);
            } catch (Exception e) {
                return R.error("推送失败");
            }
        }
        return R.ok().put("code", SUCCESS);
    }

    /**
     * ------------------4.1--------------------
     * <p>
     * -------------判定是否催单(待收币)------------
     */
    @PostMapping("checkReminders")
    public R checkReminders(@RequestParam("id") String id) {
        if (redisUtils.get(Common.ADD_LOCKNUM + id) == null || "".equals(redisUtils.get(Common.ADD_LOCKNUM + id))) {
            return R.ok().put("state", "0");
        } else {
            return R.ok().put("state", "1");
        }
    }

    /**
     * ------------------5--------------------
     * <p>
     * --------------点击申诉(待收币)---------
     */
    @PostMapping("appeal")
    @Transactional(propagation = Propagation.REQUIRED)
    public R appeal(@RequestParam("id") String id) {
        UserBuyHistoryEntity userBuyHistoryEntity = userBuyHistoryService.selectOne(new EntityWrapper<UserBuyHistoryEntity>().eq("user_buy_id", id));
        //修改订单状态
        UserBuyEntity userBuyEntity = userBuyService.selectById(id);
        userBuyEntity.setState(STATE_BUY_APPEAL);
        userBuyService.updateById(userBuyEntity);
        //添加订单到申诉表中
        UserComplaintEntity userComplaintEntity = new UserComplaintEntity();
        userComplaintEntity.setComplaintState("1");
        userComplaintEntity.setComplaintUid(userBuyEntity.getUid());
        userComplaintEntity.setContactsUid(userBuyHistoryEntity.getSellUid());
        userComplaintEntity.setCreateTime(new Date());
        userComplaintEntity.setOrderId(id);
        userComplaintEntity.setOrderState("0");
        userComplaintService.insert(userComplaintEntity);
        return R.ok().put("code", SUCCESS);
    }

    /**
     * ------------------6--------------------
     * <p>
     * --------------点击确认(待确认)-----------
     */
    @Login
    @PostMapping("PayCoin")
    @Transactional(propagation = Propagation.REQUIRED)
    public R payCoin(@RequestParam("id") String id, @RequestParam("pwd") String pwd, @RequestAttribute("uid") String uid) {

        //判断交易密码是否正确
        UserPayPwdEntity userPayPwdEntity = userUtils.selectUserPayPwd(new ModelMap("uid", uid));
        //交易密码不正确
        if (!pwd.equals(userPayPwdEntity.getPayPassword())) {
            return R.ok().put("code", "3");
        }
        //手续费
        Map<String, Float> map = this.poundage(id);
        BigDecimal buyQuantity = new BigDecimal(map.get("buyQuantity"));
        BigDecimal totalPoundage = new BigDecimal(map.get("totalPoundage"));
        //充值
        UserBuyHistoryEntity userBuyHistoryEntity = userBuyHistoryService.selectOne(new EntityWrapper<UserBuyHistoryEntity>().eq("user_buy_id", id));
        UserAccountEntity userAccountEntity = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", userBuyHistoryEntity.getPurchaseUid()));
        userAccountEntity.setRegulateIncome(userAccountEntity.getRegulateIncome().add(buyQuantity));
        userAccountEntity.setAvailableAssets(userAccountEntity.getAvailableAssets().add(buyQuantity));
        userAccountService.updateById(userAccountEntity);

        //添加手续费到user_brokerage中
        UserBrokerageEntity userBrokerageEntity = userBrokerageService.selectById(1);
        userBrokerageEntity.setSellBrokerage(userBrokerageEntity.getSellBrokerage().add(totalPoundage));
        userBrokerageService.update(userBrokerageEntity, new EntityWrapper<UserBrokerageEntity>().eq("id", 1));

        //删除TRADE_POUNDAGE
        tradePoundageService.delete(new EntityWrapper<TradePoundageEntity>().eq("user_trade_id", id));

        //设置结束时间
        userBuyHistoryEntity.setFinishTime(new Date());

        //设置状态
        UserBuyEntity userBuyEntity = userBuyService.selectById(id);
        userBuyEntity.setState(STATE_BUY_FINISH);
        userBuyService.updateById(userBuyEntity);

        //设置状态,交易成功
        userBuyHistoryEntity.setSellState(STATE_BUY_FINISH);
        userBuyHistoryEntity.setPurchaseState(STATE_BUY_FINISH);
        userBuyHistoryService.updateById(userBuyHistoryEntity);
        return R.ok().put("code", SUCCESS);
    }

    /**-------------------------------------------------------工具--------------------------------------------------------*/

    /**
     * ----------------------------手续费+订单数量------------------------
     */
    public Map<String, Float> poundage(String id) {
        UserBuyEntity userBuy = userBuyService.selectById(id);
        if (userBuy == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("#########.##");
        //交易数量
        Float buyQuantity = Float.parseFloat(df.format(userBuy.getQuantity()));
        //手续费比率
        Float poundage = userTradeConfigService.selectOne(new EntityWrapper<UserTradeConfigEntity>().eq("remark", "交易手续费")).getPoundage();
        //Float poundage = Float.parseFloat(df.format(poundagePer));
        //手续费数量
        Float totalPoundage = buyQuantity * poundage;
        //实际交易总数量
        Float totalQuantity = buyQuantity + totalPoundage;
        //单价
        Float price = userBuy.getPrice();
        //总价格
        Float totalMoney = buyQuantity * (price);

        Map<String, Float> map = new HashMap<String, Float>();
        map.put("buyQuantity", buyQuantity);
        map.put("poundage", poundage);
        map.put("totalPoundage", totalPoundage);
        map.put("totalQuantity", totalQuantity);
        map.put("price", price);
        map.put("totalMoney", totalMoney);
        return map;
    }

    /**
     * --------------------------------扣款-------------------------------
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public R deduct(BigDecimal money, String uid) {
        UserAccountEntity userAccountEntity = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", uid));
        if (userAccountEntity.getAvailableAssets().compareTo(money) != -1) {
            if (userAccountEntity.getRegulateRelease().compareTo(money) != -1) {
                userAccountEntity.setRegulateRelease(userAccountEntity.getRegulateRelease().subtract(money));
                userAccountEntity.setAvailableAssets(userAccountEntity.getAvailableAssets().subtract(money));
                userAccountService.update(userAccountEntity, new EntityWrapper<UserAccountEntity>().eq("uid", uid));
            } else {
                userAccountEntity.setRegulateIncome(userAccountEntity.getRegulateIncome().subtract(money.subtract(userAccountEntity.getRegulateRelease())));
                userAccountEntity.setRegulateRelease(new BigDecimal(0));
                userAccountEntity.setAvailableAssets(userAccountEntity.getAvailableAssets().subtract(money));
                userAccountService.update(userAccountEntity, new EntityWrapper<UserAccountEntity>().eq("uid", uid));
            }
            return R.ok();
        }
        return R.error("资金不足，扣款失败");
    }
}
