package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.annotation.UserAccount;
import cn.bitflash.feignInterface.UserFeign;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.UserBuyService;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.trade.UserBuyEntity;
import cn.bitflash.trade.UserBuyMessageBean;
import cn.bitflash.service.*;
import cn.bitflash.trade.*;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 求购
 * @author chenchengyi
 *
 * 求购状态：
 *      求购者：‘1’：可撤销； ‘2’：待收款； ‘4’：待确认   ‘6’完成
 *      卖出者：    -     ； ‘3’：待付款； ‘5’：待收币   ‘6’完成
 *      求购者：‘1’：可撤销； ‘2’：待付款； ‘4’：待收币   ‘6’完成
 *      卖出者：    -     ； ‘3’：待收款； ‘5’：待确认   ‘6’完成
 * 订单状态：
 *      已撤销：‘-1’
 *      可撤销：‘0’
 *      交易中：‘1’
 *      完成：‘2’
 */
@RestController
@RequestMapping("/api/need" )
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
    private TradePoundageService tradePoundageService;

    @Autowired
    private UserFeign userFeign;


    /**
     * 显示求购信息（交易页）
     * @param user
     * @return
     */
    @Login
    @PostMapping("showBuyMessage")
    public R showNeedMessage(@LoginUser UserEntity user, @RequestParam("pages") String pages, @UserAccount UserAccountEntity userAccount) {

        List<UserBuyMessageBean> ub = userBuyService.getBuyMessage(user.getUid(), Integer.valueOf(pages));
        if (ub == null ||ub.size() < 0) {
            return R.error("暂时没有求购信息");
        }
        Integer count = userBuyService.getNumToPaging();
        return R.ok().put("count", count).put("list", ub).put("availableAssets", userAccount.getAvailableAssets());

    }

    /**
     * 显示求购（订单页)
     */
    @Login
    @PostMapping("showBuyMessageOrder")
    public R showUserBuyMessage(@LoginUser UserEntity user) {
        List<UserBuyHistoryEntity> userBuyHistoryEntities = userBuyHistoryService.selectList(new EntityWrapper<UserBuyHistoryEntity>().eq("uid", user.getUid()));
        return R.ok();
    }

    /**
     * 显示求购详情页
     */
    @Login
    @PostMapping("showBuyMessagePage")
    public R showBuyMessagePage(@RequestParam("id") String id) {
        //订单详情
        UserBuyEntity userBuy = userBuyService.selectById(Integer.parseInt(id));
        //判定订单不存在
        if (userBuy == null) {
            return R.ok().put("code","订单不存在");
        }
        //获取手续费
        Map<String,Float> map = poundage(id);

        return R.ok().put("userBuy", userBuy).put("poundage",map.get("poundage")*100).put("totalMoney",map.get("totalMoney")).put("totalQuantity",map.get("totalMoney"));
    }

    /**
     * 手续费
     */
    public Map<String,Float> poundage(String id){
        UserBuyEntity userBuy = userBuyService.selectById(Integer.parseInt(id));
        if(userBuy == null){
            return null;
        }
        //手续费
        DecimalFormat df = new DecimalFormat("#########.##" );
        Float buyQuantity = Float.parseFloat(df.format(userBuy.getQuantity()));
        //手续费比率
        Float poundage = userTradeConfigService.selectOne(new EntityWrapper<UserTradeConfigEntity>().eq("remark", "交易手续费")).getPoundage();
        //Float poundage = Float.parseFloat(df.format(poundagePer));
        //手续费数量
        Float totalPoundage = buyQuantity*poundage;
        //交易总数量
        Float totalQuantity = buyQuantity+totalPoundage;
        //总价格
        Float totalMoney = buyQuantity*(userBuy.getPrice());

        Map<String,Float> map = new HashMap<String,Float>();
        map.put("buyQuantity",buyQuantity);
        map.put("poundage",poundage);
        map.put("totalPoundage",totalPoundage);
        map.put("totalQuantity",totalQuantity);
        map.put("totalMoney",totalMoney);

        return map;
    }

    /**
     * 下单
     * 1.查询手续费，并从卖出者账号中扣除。如资金不足抛出错误
     * 2.添加手续费到trade_poundage
     * 3.修改user_buy中state为‘1’交易中状态
     * 4.添加订单到user_buy_history
     * 5.修改求购者
     */
    @Login
    @PostMapping("addBuyMessageHistory")
    public R addBuyMessageHistory(@RequestParam("id") String id, @LoginUser UserEntity user) {

        //交易状态:'1'资金不足,'2'订单不存在
        String code = "0";

        UserBuyEntity userBuy = userBuyService.selectById(Integer.parseInt(id));
        if (userBuy == null) {
            return R.ok().put("code","2");
        }

        //手续费5%
        //BigDecimal buyQuantity = new BigDecimal(userBuy.getQuantity());
        //BigDecimal poundage = new BigDecimal(userTradeConfigService.selectOne(new EntityWrapper<UserTradeConfigEntity>().eq("remark", "交易手续费")).getPoundage());
        //BigDecimal totalPoundage = buyQuantity.multiply(poundage);
        //交易总数量
        //BigDecimal totalQuantity = buyQuantity.add(totalPoundage);

        //获取手续费
        Map<String,Float> map = poundage(id);

        //扣除手续费
        UserAccountEntity userAccountEntity = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", user.getUid()));
        //不足支付扣款
        if (new BigDecimal(map.get("totalQuantity")).compareTo(userAccountEntity.getAvailableAssets()) == 1) {
            code = "1";
            return R.ok().put("code", code);
        }
        deduct(new BigDecimal(map.get("totalPoundage")), user.getUid());

        //添加手续费记录
        TradePoundageEntity tradePoundageEntity = new TradePoundageEntity();
        tradePoundageEntity.setCreateTime(new Date());
        tradePoundageEntity.setPoundage(new BigDecimal(map.get("totalPoundage")));
        tradePoundageEntity.setUid(user.getUid());
        tradePoundageEntity.setUserTradeId(Integer.parseInt(id));
        tradePoundageService.insert(tradePoundageEntity);

        //修改user_buy订单状态
        userBuy.setState("1");
        userBuyService.update(userBuy,new EntityWrapper<UserBuyEntity>().eq("id",Integer.parseInt(id)));

        //添加订单到user_buy_history
        UserBuyHistoryEntity buyHistory = new UserBuyHistoryEntity();
        buyHistory.setPrice(new BigDecimal(userBuy.getPrice()));
        buyHistory.setPurchaseState("2");
        buyHistory.setPurchaseUid(userBuy.getUid());
        buyHistory.setQuantity(new BigDecimal(userBuy.getQuantity()));
        buyHistory.setSellState("3");
        buyHistory.setSellUid(user.getUid());
        buyHistory.setUserBuyId(Integer.parseInt(id));
        userBuyHistoryService.insert(buyHistory);

        return R.ok().put("code", code);
    }

    /**
     * 查看(订单页)
     */
    @Login
    @PostMapping("checkOrder")
    public R checkOrder(@RequestParam("id") String id, @LoginUser UserEntity user){
        UserBuyHistoryEntity userBuyHistoryEntity = userBuyHistoryService.selectOne(new EntityWrapper<UserBuyHistoryEntity>().eq("user_buy_id", id));
        String uid = userBuyHistoryEntity.getSellUid();
        //查询卖出者信息
        UserInfoEntity userInfo = userFeign.selectUserInfoById(uid);
        return R.ok().put("userBuyHistoryEntity",userBuyHistoryEntity).put("userInfo",userInfo);
    }


    /**
     * 购买者付款成功
     */
    @Login
    @PostMapping("payMoney")
    public R payMoney(@RequestParam("id") String id) {

        //设置支付时间
        UserBuyEntity userBuyEntity = userBuyService.selectById(Integer.parseInt(id));
        userBuyEntity.setPay_time(new Date());

        //修改交易状态
        UserBuyHistoryEntity userBuyHistoryEntity = userBuyHistoryService.selectOne(new EntityWrapper<UserBuyHistoryEntity>().eq("user_buy_id", id));
        userBuyHistoryEntity.setSellState("5");
        userBuyHistoryEntity.setPurchaseState("4");

        return R.ok();
    }

    /**
     * 卖出者发币
     */
    @Login
    @PostMapping("PayCoin")
    public R PayCoin(@RequestParam("id") String id, @RequestParam("pwd") String pwd, @RequestParam("uid") String uid, @LoginUser UserEntity user) {

        //判断交易密码是否正确
        UserPayPwdEntity userPayPwdEntity = userFeign.selectUserPayPwd(new ModelMap("uid", user.getUid()));
        //交易密码不正确
        if (!pwd.equals(userPayPwdEntity.getPayPassword())) {
            return R.ok().put("code", "3");
        }

        //手续费5%
        UserBuyEntity userBuy = userBuyService.selectById(Integer.parseInt(id));
        BigDecimal buyQuantity = new BigDecimal(userBuy.getQuantity());
        BigDecimal poundage = new BigDecimal(userTradeConfigService.selectOne(new EntityWrapper<UserTradeConfigEntity>().eq("remark", "交易手续费")).getPoundage());
        BigDecimal totalPoundage = buyQuantity.multiply(poundage);

        //扣款
        deduct(buyQuantity, uid);

        //充值
        UserAccountEntity userAccountEntity = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", user.getUid()));
        userAccountEntity.setRegulateRelease(buyQuantity);
        userAccountEntity.setAvailableAssets(buyQuantity);

        //添加手续费到user_brokerage中
        UserBrokerageEntity userBrokerageEntity = userBrokerageService.selectById(1);
        userBrokerageEntity.setSellBrokerage(userBrokerageEntity.getSellBrokerage().add(totalPoundage));
        userBrokerageService.update(userBrokerageEntity, new EntityWrapper<UserBrokerageEntity>().eq("id", 1));

        //删除TRADE_POUNDAGE
        tradePoundageService.delete(new EntityWrapper<TradePoundageEntity>().eq("userTradeId", id));

        //设置结束时间
        UserBuyHistoryEntity userBuyHistoryEntity = userBuyHistoryService.selectOne(new EntityWrapper<UserBuyHistoryEntity>().eq("user_buy_id", id));
        userBuyHistoryEntity.setFinishTime(new Date());

        //设置状态,交易成功
        userBuyHistoryEntity.setSellState("6");
        userBuyHistoryEntity.setPurchaseState("6");
        return R.ok();
    }


    /**
     * 扣款
     */
    public int deduct(BigDecimal money, String uid) {

        UserAccountEntity userAccountEntity = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", uid));
        if (userAccountEntity.getRegulateRelease().compareTo(money) != -1) {
            userAccountEntity.setRegulateRelease(userAccountEntity.getRegulateRelease().subtract(money));
            userAccountEntity.setAvailableAssets(userAccountEntity.getAvailableAssets().subtract(money));
            userAccountService.update(userAccountEntity, new EntityWrapper<UserAccountEntity>().eq("uid", uid));
        } else {
            userAccountEntity.setRegulateRelease(new BigDecimal(0));
            userAccountEntity.setRegulateIncome(userAccountEntity.getRegulateIncome().subtract(money));
            userAccountEntity.setAvailableAssets(userAccountEntity.getAvailableAssets().subtract(money));
            userAccountService.update(userAccountEntity, new EntityWrapper<UserAccountEntity>().eq("uid", uid));
        }

        return 1;
    }

    /**
     * 添加求购信息
     *
     * @param userBuyEntity
     * @param user
     * @return
     */
    @Login
    @PostMapping("addBuyMessage")
    public R addBuyMessage(@RequestBody UserBuyEntity userBuyEntity, @LoginUser UserEntity user) {
        if (userBuyEntity == null) {
            return R.error(501, "求购信息为空");
        }
        Float num = userBuyEntity.getQuantity();
        if (num % 100 != 0 || num < 100) {
            return R.error(502, "求购数量最低为100，且为100的倍数");
        }
        userBuyEntity.setCreate_time(new Date());
        userBuyEntity.setState("0");
        userBuyService.addBuyMessage(userBuyEntity, user.getUid());
        return R.ok();
    }

    /**
     * 撤销求购信息
     *
     * @param id
     * @param user
     * @return
     */
    @Login
    @PostMapping("cancel")
    public R cancelBuyMessage(@RequestParam String id, @LoginUser UserEntity user) {
        UserBuyEntity ub = userBuyService.selectById(id);
        if (ub == null) {
            return R.error(501, "信息不存在");
        }
        if (ub.getState().equals("-1")) {
            return R.error(502, "信息已经撤销");
        }
        if (ub.getState().equals("0")) {
            ub.setState("0");
            ub.setState("-1");
            ub.setCancel_time(new Date());
            userBuyService.updateById(ub);
            return R.ok().put("code","200");
        }
        return R.ok();
    }

        /**
         * 显示求购者详情
         * @param id
         * @return
         */

//    @Login
//    @PostMapping("showDetailed" )
//    public R showDetailed(@RequestParam String id) {
//        UserBuyEntity ub = userBuyService.selectById(id);
//        if (ub.getUid() == null) {
//            return R.error(501, "订单不存在" );
//        }
//        UserInfoEntity ui = userFeign.selectUserInfoById(ub.getUid());
//        if (ui == null) {
//            return R.error(502, "卖出者信息不存在" );
//        }
//        return R.ok().put("buy", ub).put("user", ui);
//    }

    }
