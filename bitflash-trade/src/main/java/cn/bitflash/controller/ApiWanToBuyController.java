package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.annotation.UserAccount;
import cn.bitflash.feignInterface.LoginFeign;
import cn.bitflash.feignInterface.SysFeign;
import cn.bitflash.feignInterface.UserFeign;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.UserBuyService;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.trade.UserBuyEntity;
import cn.bitflash.trade.UserBuyMessageBean;
import cn.bitflash.service.*;
import cn.bitflash.trade.*;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.utils.GeTuiSendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 求购
 * @author chenchengyi
 *
 * 求购状态：
 *      求购者：‘1’：可撤销； ‘2’：待收款； ‘4’：待确认   ‘6’完成
 *      卖出者：    -     ； ‘3’：待付款； ‘5’：待收币   ‘6’完成  ‘9’申诉
 * 订单状态：
 *      已撤销：‘0’
 *      可撤销：‘1’
 *      ‘2’：待收款
 *      ‘3’：待付款
 *      ‘4’：待确认
 *      ‘5’：待收币
 *      ‘6’完成
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

    @Autowired
    private LoginFeign loginFeign;

    @Autowired
    private SysFeign sysFeign;



    /**-----------------------------------------------显示求购信息列表-----------------------------------------------------*/

    /**
     * ----------------交易页-----------------
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
     * ---------------订单页----------------
     */
    @Login
    @PostMapping("showBuyMessageOwn")
    public R showUserBuyMessage(@LoginUser UserEntity user) {
        List<UserBuyBean> userBuyEntities = userBuyService.selectBuyList(user.getUid());
        List<UserBuyBean> userBuyEntitiesList = new LinkedList<UserBuyBean>();
        String state = null;
        for(UserBuyBean userBuyEntity : userBuyEntities){
            if("1".equals(userBuyEntity.getState()) || "1".equals(userBuyEntity.getSellState())){
                state = "可撤销";
            }
            if("2".equals(userBuyEntity.getState()) || "2".equals(userBuyEntity.getSellState())){
                state = "待收款";
            }
            if("3".equals(userBuyEntity.getState()) || "3".equals(userBuyEntity.getSellState())){
                state = "待付款";
            }
            if("4".equals(userBuyEntity.getState()) || "4".equals(userBuyEntity.getSellState())){
                state = "待确认";
            }
            if("5".equals(userBuyEntity.getState()) || "5".equals(userBuyEntity.getSellState())){
                state = "待收币";
            }
            userBuyEntity.setState(state);
            userBuyEntitiesList.add(userBuyEntity);
        }

        return R.ok().put("userBuyEntitiesList",userBuyEntitiesList);
    }

    /**--------------------------------------------------添加订单---------------------------------------------------------*/

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
        userBuyService.addBuyMessage(userBuyEntity, user.getUid());
        return R.ok();
    }

    /**-----------------------------------------------------下单---------------------------------------------------------*/

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
        userBuy.setState("2");
        userBuyService.update(userBuy,new EntityWrapper<UserBuyEntity>().eq("id",Integer.parseInt(id)));

        //添加订单到user_buy_history
        UserBuyHistoryEntity buyHistory = new UserBuyHistoryEntity();
        buyHistory.setPrice(new BigDecimal(userBuy.getPrice()));
        buyHistory.setPurchaseState("3");
        buyHistory.setPurchaseUid(userBuy.getUid());
        buyHistory.setQuantity(new BigDecimal(userBuy.getQuantity()));
        buyHistory.setSellState("2");
        buyHistory.setSellUid(user.getUid());
        buyHistory.setUserBuyId(Integer.parseInt(id));
        userBuyHistoryService.insert(buyHistory);

        return R.ok().put("code", code);
    }

    /**-------------------------------------------查看交易页求购详情页-----------------------------------------------------*/

    /**
     * 查看交易详情
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

        return R.ok().put("userBuy", userBuy).put("poundage",map.get("poundage")*100).put("totalMoney",map.get("totalMoney")).put("totalQuantity",map.get("totalQuantity"));
    }

    /**-------------------------------------------查看订单页求购详情页-----------------------------------------------------*/

    /**
     * 订单页
     */
    @PostMapping("checkOrder")
    public R checkOrder(@RequestParam("id") String id){

        UserBuyHistoryBean userBuyHistoryBean = userBuyHistoryService.selectBuyHistory(id);

        Map<String,Float> map = this.poundage(id);

        return R.ok().put("userBuyHistoryBean",userBuyHistoryBean).put("price",map.get("price")).put("buyQuantity",map.get("buyQuantity")).put("totalMoney",map.get("totalMoney"));
    }

    /**-------------------------------------------------点击操作----------------------------------------------------------*/

    /** -----------------1-----------------
     *
     *  --------------点击撤销--------------
     */
    @PostMapping("cancel")
    public R cancelBuyMessage(@RequestParam String id) {
        UserBuyEntity ub = userBuyService.selectById(id);
        if (ub == null) {
            return R.error(501, "交易不存在");
        }
        if (ub.getState().equals("0")) {
            return R.error(502, "交易已经撤销");
        }
        if (ub.getState().equals("1")) {
            ub.setState("0");
            ub.setCancelTime(new Date());
            userBuyService.updateById(ub);
            return R.ok().put("code","0");
        }
        return R.ok();
    }

    /** ---------------2-------------------
     *
     *  --------点击已付款(待付款)-----------
     */
    @PostMapping("payMoney")
    public R payMoney(@RequestParam("id") String id) {
        UserBuyEntity userBuyEntity = userBuyService.selectById(Integer.parseInt(id));
        if (!userBuyEntity.getState().equals("3")) {
            return R.ok().put("code","1");
        }

        //设置支付时间
        userBuyEntity.setPayTime(new Date());
        userBuyService.update(userBuyEntity,new EntityWrapper<UserBuyEntity>().eq("id",id));

        //修改user_buy订单状态
        UserBuyEntity userBuy = userBuyService.selectById(Integer.parseInt(id));
        userBuy.setState("5");
        userBuyService.update(userBuy,new EntityWrapper<UserBuyEntity>().eq("id",Integer.parseInt(id)));
        //修改交易状态
        UserBuyHistoryEntity userBuyHistoryEntity = userBuyHistoryService.selectOne(new EntityWrapper<UserBuyHistoryEntity>().eq("user_buy_id", id));
        userBuyHistoryEntity.setSellState("4");
        userBuyHistoryEntity.setPurchaseState("5");
        userBuyHistoryService.update(userBuyHistoryEntity,new EntityWrapper<UserBuyHistoryEntity>().eq("user_buy_id", id));

        return R.ok().put("code","0");
    }

    /** ------------------3--------------------
     *
     *  -------------点击取消(待付款)------------
     */

    /** ------------------4--------------------
     *
     *  -------------点击催单(待收币)------------
     */
    @Login
    @PostMapping("reminders")
    public R reminders(@RequestParam("id") String id){

        UserBuyHistoryEntity userBuyHistoryEntity = userBuyHistoryService.selectOne(new EntityWrapper<UserBuyHistoryEntity>().eq("user_buy_id",id));
        //获取Cid
        String cid = loginFeign.selectGT(new ModelMap("uid",userBuyHistoryEntity.getSellUid())).getCid();
        //获取推送信息
        String text = userBuyHistoryEntity.getUserBuyId()+sysFeign.getVal("reminders");

        try {
            GeTuiSendMessage.sendSingleMessage( text, cid);
        }catch (Exception e){
            return R.error("推送失败");
        }
        return R.ok().put("code","0");
    }

    /** ------------------5--------------------
     *
     *  --------------点击申诉中(待收币)---------
     */

    /** ------------------6--------------------
     *
     *  --------------点击确认(待确认)-----------
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

        //手续费
        Map<String,Float> map = this.poundage(id);
        BigDecimal buyQuantity = new BigDecimal(map.get("buyQuantity"));
        BigDecimal totalPoundage = new BigDecimal(map.get("totalPoundage"));

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

    /**-------------------------------------------工具---------------------------------------------*/

    /**
     * -----------------手续费+订单数量------------------
     *
     */
    public Map<String,Float> poundage(String id){
        UserBuyEntity userBuy = userBuyService.selectById(Integer.parseInt(id));
        if(userBuy == null){
            return null;
        }

        DecimalFormat df = new DecimalFormat("#########.##" );
        //交易数量
        Float buyQuantity = Float.parseFloat(df.format(userBuy.getQuantity()));
        //手续费比率
        Float poundage = userTradeConfigService.selectOne(new EntityWrapper<UserTradeConfigEntity>().eq("remark", "交易手续费")).getPoundage();
        //Float poundage = Float.parseFloat(df.format(poundagePer));
        //手续费数量
        Float totalPoundage = buyQuantity*poundage;
        //实际交易总数量
        Float totalQuantity = buyQuantity+totalPoundage;
        //单价
        Float price = userBuy.getPrice();
        //总价格
        Float totalMoney = buyQuantity*(price);

        Map<String,Float> map = new HashMap<String,Float>();
        map.put("buyQuantity",buyQuantity);
        map.put("poundage",poundage);
        map.put("totalPoundage",totalPoundage);
        map.put("totalQuantity",totalQuantity);
        map.put("price",price);
        map.put("totalMoney",totalMoney);
        return map;
    }

    /**
     * --------------------------------扣款-------------------------------
     *
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

}
