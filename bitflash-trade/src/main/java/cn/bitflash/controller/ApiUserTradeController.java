package cn.bitflash.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bitflash.FeignController.TradeFeign;
import cn.bitflash.trade.*;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.user.UserEntity;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.utils.Common;
import common.utils.R;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.annotation.PayPassword;
import cn.bitflash.annotation.UserAccount;

import cn.bitflash.service.UserAccountService;
import cn.bitflash.service.UserBrokerageService;
import cn.bitflash.service.UserPayUrlService;
import cn.bitflash.service.UserTradeHistoryService;
import cn.bitflash.service.UserTradeLockService;
import cn.bitflash.service.UserTradeService;


/**
 * @author wangjun
 * @version 2018年7月4日上午9:44:17
 */

@RestController
@RequestMapping("/api")
public class ApiUserTradeController {

    private final Logger logger = LoggerFactory.getLogger(ApiUserTradeController.class);

    @Autowired
    private TradeFeign tradeFeign;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserTradeHistoryService userTradeHistoryService;

    @Autowired
    private UserTradeService userTradeService;

    @Autowired
    private UserTradeLockService userTradeLockService;

    @Autowired
    private UserBrokerageService userBrokerageService;

//    @Autowired
//    private PlatFormConfigService platFormConfigService;

    @Autowired
    private UserPayUrlService userPayUrlService;

    /**
     * @param userAccount
     * @param pageNum     第几页
     * @return
     */
    @PostMapping("tradeList")
    public R tradeList(@UserAccount UserAccountEntity userAccount, @RequestParam String pageNum, @LoginUser UserEntity user) {

        Map<String,Object> aa = new HashMap<String,Object>();
        aa.put("uid","3333");
        List<UserEntity> list = tradeFeign.selectOne(aa);


        int pageTotal = 6;
        Map<String, Object> param = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(user.getMobile())) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("uid", userAccount.getUid());
            map.put("pageNum", new Integer(pageNum));
            map.put("pageTotal", new Integer(pageTotal));
            // 查询自身用户信息
            List<UserTradeEntity> listEntity = userTradeService.queryTrade(map);

            Integer count = userTradeService.selectTradeCount(map);

            param.put("availableAssets", userAccount.getAvailableAssets());
            param.put("userAccountList", listEntity);
            param.put("totalRecord", count);
        } else {
            return R.error("无此用户！");
        }
        return R.ok().put("userAccount", param);
    }


    @PostMapping("tradeListTest")
    public R tradeList() {

        System.out.print("999999999999999999");
        Map<String,Object> aa = new HashMap<String,Object>();
        aa.put("uid","9AA233965DC14499AADC890081268732");
        List<UserEntity> list = tradeFeign.selectOne(aa);
        System.out.print(list.size());

        return R.ok().put("userAccount", "");
    }


    @Login
    @PostMapping("responseTrade")
    public R responseTrade(@UserAccount UserAccountEntity userAccount) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("uid", userAccount.getUid());

        List<UserAccountEntity> userAccountEntity = userAccountService.selectByMap(param);

        Map<String, Object> returnMap = null;
        if (null != userAccountEntity) {

            returnMap = userTradeService.selectTrade(param);
            returnMap.put("availableAssets", userAccountEntity.get(0).getAvailableAssets());
        }
        return R.ok().put("userAccount", returnMap);
    }

    @Login
    @PostMapping("forwardPay")
    public R forwardPay(@RequestParam String id) {
        if (StringUtils.isNotBlank(id)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            logger.info("订单号:" + id);

            List<Map<String, Object>> list = userTradeService.selectTradeUrl(map);
            Map<String, Object> returnMap1 = null;
            if (null != list && list.size() > 0) {

                returnMap1 = list.get(0);
                returnMap1.put("wechatUrl", returnMap1.get("imgUrl"));
                if (list.size() > 1) {
                    Map<String, Object> returnMap2 = list.get(1);
                    if (null != returnMap2.get("imgUrl")) {
                        returnMap1.put("payUrl", returnMap2.get("imgUrl"));
                    }
                }
            }
            return R.ok().put("payMap", returnMap1);

        } else {
            return R.error("参数不能为空！");
        }
    }

    @Login
    @PostMapping("saveTrade")
    @Transactional
    public R saveTrade(@UserAccount UserAccountEntity userAccount, @RequestParam String quantity, @RequestParam String price) {

        // 先校验出售数量是否大于已有数量
        BigDecimal total = userAccount.getAvailableAssets();
        logger.info("uid:" + userAccount.getUid() + ",卖出数量:" + quantity + ",价格:" + price);
        if (StringUtils.isNotBlank(price) && !"0".equals(quantity)) {

            BigDecimal priceB = new BigDecimal(price);
            BigDecimal minPrice = new BigDecimal(Common.MIN_PRICE);
            if (priceB.compareTo(minPrice) <= -1) {
                return R.error("最低价格为0.325!");
            }
            BigDecimal quantityB = new BigDecimal(quantity);

            // 卖出数量
            double quantityD = Double.parseDouble(quantity);
            if (quantityD % Common.MULTIPLE == 0) {
                // 手续费 = 卖出数量 * 0.01
                double percent = quantityD * 0.01;
                logger.info("手续费:" + percent);

                BigDecimal percentB = new BigDecimal(Math.floor(percent));
                // 总卖出数量 = 卖出数量 + 手续费
                BigDecimal purchase = quantityB.add(percentB);
                // 等于1表示total大于percentB,可以交易
                if (total.compareTo(purchase) == 1 || total.compareTo(purchase) == 0) {
                    // 1.先扣除手续费
                    // 手续费 = 总可用-百分比
                    UserBrokerageEntity userBrokerageEntity = userBrokerageService.selectById("1");
                    if (null != userBrokerageEntity) {
                        BigDecimal sellBrokerage = userBrokerageEntity.getSellBrokerage();
                        BigDecimal brokerage = sellBrokerage.add(percentB);
                        userBrokerageEntity.setSellBrokerage(brokerage);
                        userBrokerageService.insertOrUpdate(userBrokerageEntity);

                        // 2.卖出数量
                        BigDecimal quantityBig = new BigDecimal(quantity);
                        // 卖出量-调节释放
                        BigDecimal subtract = quantityBig.subtract(userAccount.getRegulateRelease());
                        if (subtract.doubleValue() > 0 || subtract.doubleValue() == 0) {
                            userAccount.setRegulateRelease(new BigDecimal(0.00));
                            BigDecimal regulateIncome = userAccount.getRegulateIncome().subtract(subtract);
                            userAccount.setRegulateIncome(regulateIncome.subtract(percentB));
                            BigDecimal availableAssets = userAccount.getAvailableAssets().subtract(new BigDecimal(quantity));
                            userAccount.setAvailableAssets(availableAssets.subtract(percentB));
                        } else {

                            BigDecimal release = quantityBig.add(percentB);
                            BigDecimal availableAssets = userAccount.getAvailableAssets().subtract(new BigDecimal(quantity));
                            userAccount.setAvailableAssets(availableAssets.subtract(percentB));
                            BigDecimal RegulatRelease = userAccount.getRegulateRelease().subtract(release);
                            userAccount.setRegulateRelease(RegulatRelease);
                        }
                        userAccountService.updateById(userAccount);

                        // 添加卖出记录
                        UserTradeEntity userTrade = new UserTradeEntity();
                        userTrade.setPrice(new BigDecimal(price));
                        userTrade.setState(Common.STATE_SELL);
                        userTrade.setQuantity(new BigDecimal(quantity));
                        userTrade.setUid(userAccount.getUid());
                        userTrade.setCreateTime(new Date());
                        userTradeService.insertUserTrade(userTrade);
                        return R.ok();
                    } else {
                        R.error();
                    }
                } else {
                    return R.error().put("code", "1");
                }
            } else {
                return R.error("卖出数量必须为1000的倍数！");
            }
        } else {
            return R.error("参数不能为空！");
        }
        return R.ok();
    }

    @Login
    @PostMapping("listTrade")
    public R listTrade(@LoginUser UserEntity user, @RequestParam String state) {
        Map<String, Object> param = new HashMap<String, Object>();
        List<UserTradeBean> list = null;
        if (StringUtils.isNotBlank(state)) {
            param.put("uid", user.getUid());
            if (state.equals("1")) {
                param.put("state", Common.STATE_PAY);
            } else {
                param.put("state", state);
            }
            list = userTradeService.selectTradeHistory(param);
        }
        return R.ok().put("tradeHistoryList", list);
    }

    @Login
    @PostMapping("cancelTrade")
    @Transactional
    public R cancelTrade(@RequestParam String id) {
        if (StringUtils.isNotBlank(id)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            // 只查询状态为卖出(1)
            map.put("state", Common.STATE_SELL);
            UserTradeBean userTradeBean = userTradeService.queryDetail(map);
            logger.info("撤消人:" + userTradeBean.getRealname() + ",撤消订单号:" + id + ",撤消数量:" + userTradeBean.getPrice());

            if (null != userTradeBean) {
                UserAccountEntity userAccountEntity = userAccountService.selectById(userTradeBean.getUid());
                if (null != userAccountEntity) {
                    // 撤消后要把卖出数量返回到user_account中
                    // 调节收入+卖出数量
                    BigDecimal regulateIncome = userAccountEntity.getRegulateIncome().add(userTradeBean.getQuantity());
                    BigDecimal availableAssets = userAccountEntity.getRegulateRelease().add(regulateIncome);
                    userAccountEntity.setAvailableAssets(availableAssets);
                    userAccountEntity.setRegulateIncome(regulateIncome);
                    userAccountService.updateById(userAccountEntity);

                    // 更新状态为撤消
                    map.put("state", Common.STATE_CANCEL);
                    userTradeService.updateTrade(map);
                } else {
                    R.error("该用户不存在！");
                }
            }
        }
        return R.ok();
    }

    /**
     * 取消订单后状态变为卖出(1)，并且删除user_trade_lock表中的数据
     *
     * @param id 订单id
     * @return
     */
    @Login
    @PostMapping("cancelOrder")
    @Transactional
    public R cancelOrder(@RequestParam String id) {
        if (StringUtils.isNotBlank(id)) {
            logger.info("取消订单号:" + id);

            // 删除锁定订单
            userTradeLockService.deleteById(id);

            // 更新
            UserTradeEntity userTradeEntity = new UserTradeEntity();
            userTradeEntity.setId(Integer.parseInt(id));
            userTradeEntity.setState(Common.STATE_SELL);
            userTradeService.insertOrUpdate(userTradeEntity);
        }
        return R.ok();
    }

    /**
     * @param id     订单id
     * @param mobile 购买人手机号
     * @return
     */
    @Login
    @PostMapping("purchase")
    @Transactional
    public R purchase(@LoginUser UserEntity user, @RequestParam String id, @RequestParam String mobile, @RequestParam String payPwd, @PayPassword UserPayPwdEntity userPayPwd) {
        if (userPayPwd.getPayPassword().equals(payPwd)) {
            if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(mobile)) {

                logger.info("购买人:" + user.getUid() + ",订单号:" + id);
                // 查询订单状态为已锁定(state:5)
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("id", id);
                UserTradeBean userTradeBean = userTradeService.queryDetail(param);
                if (null != userTradeBean) {
                    // 订单状态变为已付款
                    userTradeBean.setState(Common.STATE_PAY);
                    userTradeBean.setId(new Integer(id));
                    param.put("state", Common.STATE_PAY);
                    param.put("finishTime", new Date());
                    userTradeService.updateTrade(param);

                    // 更新购买者收益
                    BigDecimal quantity = userTradeBean.getQuantity();

                    Map<String, Object> map = new HashMap<String, Object>();

                    // 查询购买人信息
                    UserTradeHistoryEntity userTradeHistoryEntity = userTradeHistoryService.selectOne(new EntityWrapper<UserTradeHistoryEntity>().eq("user_trade_id", id));

                    if (null != userTradeHistoryEntity) {
                        map.put("uid", userTradeHistoryEntity.getPurchaseUid());
                        List<UserAccountEntity> userAccountList = userAccountService.selectByMap(map);
                        UserAccountEntity userAccount = null;
                        if (null != userAccountList && userAccountList.size() > 0) {
                            userAccount = userAccountList.get(0);

                            // 计算手续费
                            // 手续费=卖出数量*0.01
                            BigDecimal multiply = quantity.multiply(new BigDecimal("0.01"));
                            UserBrokerageEntity userBrokerageEntity = userBrokerageService.selectById("1");
                            if (null != userBrokerageEntity) {
                                double multiplyd = multiply.doubleValue();
                                BigDecimal multiplyB = userBrokerageEntity.getPurchaseBrokerage().add(new BigDecimal(Math.floor(multiplyd)));
                                userBrokerageEntity.setPurchaseBrokerage(multiplyB);
                                userBrokerageService.insertOrUpdate(userBrokerageEntity);

                                // 购买量=买出数量-手续费
                                BigDecimal purchase = quantity.subtract(multiply);

                                BigDecimal regulateIncome = userAccount.getRegulateIncome().add(purchase);

                                userAccount.setRegulateIncome(regulateIncome);
                                BigDecimal availableAssets = userAccount.getAvailableAssets().add(purchase);
                                userAccount.setAvailableAssets(availableAssets);
                                userAccount.setUid(userTradeHistoryEntity.getPurchaseUid());
                                userAccountService.updateUserAccountByParam(userAccount);

                                // 添加购买记录
                                UserTradeHistoryEntity userTradeHistory = new UserTradeHistoryEntity();
                                userTradeHistory.setUserTradeId(userTradeBean.getId());
                                userTradeHistory.setSellUid(user.getUid());
                                userTradeHistory.setCreateTime(new Date());
                                userTradeHistory.setSellQuantity(purchase);
                                userTradeHistory.setState(Common.STATE_PAY);
                                userTradeHistory.setPrice(userTradeBean.getPrice());
                                userTradeHistory.setCreateTime(new Date());
                                userTradeHistoryService.updateUserTradeHistory(userTradeHistory);
                            }
                        }
                    }
                } else {
                    R.error("订单不存在！");
                }
            }
            return R.ok();
        }
        return R.error("交易密码错误");

    }

    /**
     * @param id 订单id
     * @return
     */
    @Login
    @PostMapping("viewDetail")
    public R viewDetail(@RequestParam String id) {
        if (StringUtils.isNotBlank(id)) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userTradeId", id);
            List<UserTradeHistoryBean> list = userTradeHistoryService.selectTradeHistory(param);
            UserTradeHistoryBean userTradeHistoryBean = null;
            if (null != list && list.size() > 0) {
                userTradeHistoryBean = list.get(0);
            }
            String uid = userTradeHistoryBean.getPurchaseUid();

            List<UserPayUrlEntity> url = userPayUrlService.selectList(new EntityWrapper<UserPayUrlEntity>().eq("uid", uid));
            try {
                String aliurl = url.stream().filter((u) -> u.getImgType().equals("2")).findFirst().get().getImgUrl();
                String wxurl = url.stream().filter((u) -> u.getImgType().equals("1")).findFirst().get().getImgUrl();
                return R.ok().put("userTrade", userTradeHistoryBean).put("wxUrl", wxurl)
                        .put("aliUrl", aliurl);
            } catch (Exception e) {
                return R.ok().put("userTrade", userTradeHistoryBean);
            }

        } else {
            return R.error("参数不能为空！");
        }
    }

    /**
     * @param id          订单id
     * @param userAccount
     * @return
     */
    @Login
    @PostMapping("payTrade")
    @Transactional
    public R payTrade(@RequestParam String id, @UserAccount UserAccountEntity userAccount) {
        if (StringUtils.isNotBlank(id)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);

            UserTradeBean uesrTradeBean = userTradeService.queryDetail(map);
            logger.info("订单号:" + id + ",付款人:" + uesrTradeBean.getRealname() + ",数量:" + uesrTradeBean.getQuantity() + ",价格:" + uesrTradeBean.getPrice());


            if (null != uesrTradeBean) {
                map.put("state", Common.STATE_PURCHASE);
                // 更新为已购买
                userTradeService.updateTrade(map);
                UserTradeHistoryEntity userTradeHistory = new UserTradeHistoryEntity();
                userTradeHistory.setUserTradeId(new Integer(id));
                userTradeHistory.setSellUid(uesrTradeBean.getUid());
                userTradeHistory.setSellQuantity(uesrTradeBean.getQuantity());
                userTradeHistory.setPrice(uesrTradeBean.getPrice());
                userTradeHistory.setPurchaseUid(userAccount.getUid());
                userTradeHistory.setPurchaseQuantity(uesrTradeBean.getQuantity());
                userTradeHistory.setCreateTime(new Date());
                userTradeHistory.setState(Common.STATE_CONFIRM);
                userTradeHistoryService.insert(userTradeHistory);
                return R.ok();
            } else {
                return R.error("未查询到该用户信息！");
            }

        } else {
            return R.error("参数不能为空！");
        }
    }

    /**
     * @param id 订单id
     * @author chen

    @Login
    @PostMapping("provingState")
    public R provingState(@RequestParam String id, @LoginUser UserEntity user) {
        String uid = user.getUid();
        switch (this.provingTime(id, uid)) {
            case 1:
                return R.ok().put("code", 200);
            case 2:
                return R.ok().put("code", 400);
            case 3:
                return R.error("订单已锁定");
            case 4:
                return R.ok().put("code", 200);
            case 5:
                return R.error(502, "订单已经被撤销");
            default:
                return R.error("系统异常");
        }
    }
     */
    /**
     * @param id 订单id
     * @author chen

    @Login
    @PostMapping("addLock")
    public R addLock(@RequestParam String id, @LoginUser UserEntity user) throws ParseException {
        String uid = user.getUid();
        Integer lock = userTradeLockService.selectByDay(user.getUid());
        if (lock >= 3) {
            return R.error(501, "当天锁定数量已到上限");
        }
        if (this.provingTime(id, uid) == 5) {
            return R.error(502, "订单已经被锁定");
        }
        if (this.provingTime(id, uid) == 3 || this.provingTime(id, uid) == 2) {
            return R.error("订单已经锁定");
        }
        UserTradeLockEntity userLock = new UserTradeLockEntity();
        userLock.setLockTime(new Date());
        userLock.setLockUid(uid);
        userLock.setUserTradeId(id);
        userTradeLockService.insertOrUpdate(userLock);
        UserTradeEntity userTradeEntity = userTradeService.selectById(id);
        userTradeEntity.setState("5");
        userTradeService.updateById(userTradeEntity);
        return R.ok().put("code", 200);
    }
     */
    /**
     * 判断时间是否超时
     *
     * @param id  账单id
     * @param uid 操作人uid
     * @author chen 1.没数据锁定 2.操作人自己锁定 3.其他人锁定 4.锁定时间超时 5撤销的订单

    public int provingTime(String id, String uid) {
        UserTradeEntity userTradeEntity = userTradeService.selectById(id);
        if (userTradeEntity.getState().equals(Common.STATE_CANCEL)) {
            return 5;
        }
        UserTradeLockEntity userTradeLockEntity = userTradeLockService.selectOne(new EntityWrapper<UserTradeLockEntity>().eq("user_trade_id", id));
        if (userTradeLockEntity == null) {
            // 订单锁表中没有数据，代表没有锁定
            return 1;
        }

        if (userTradeLockEntity.getLockTime() != null) {
            Date time = new Date(userTradeLockEntity.getLockTime().getTime() + Integer.valueOf(platFormConfigService.getVal(Common.OUT_TIME)));
            // 如果时间没有超时
            if (time.getTime() > new Date().getTime()) {
                if (userTradeLockEntity.getLockUid().equals(uid)) {
                    // 时间超时，且为锁定人操作，提示不锁定
                    return 2;
                } else {
                    // 锁定时间没有超时，不为锁定人操作。提示锁定
                    return 3;
                }
            } else {
                // 超时
                return 4;
            }
        }
        return 1;
    }
     */
    /**
     * @author chen

    @Login
    @PostMapping("updateTradeState")
    public R updateTradeState(@LoginUser UserEntity user) {
        List<Map<String, Object>> mapList = userTradeService.getHistoryBystate5();
        for (Map<String, Object> map : mapList) {
            String id = map.get("userTradeId").toString();
            String uid = user.getUid();
            int result = this.provingTime(id, uid);
            if (result == 4) {
                //  UserTradeEntity userTradeEntity2 = userTradeService.selectById(id);
                UserTradeEntity userTradeEntity2 = new UserTradeEntity();
                userTradeEntity2.setId(Integer.valueOf(id));
                userTradeEntity2.setState("1");
                userTradeService.updateById(userTradeEntity2);
            }
        }
        return R.ok();
    }
     */
    @Login
    @PostMapping("buyMessage")
    public R buyMessage(@RequestParam String id) {
        return R.ok().put("buyMessage", userTradeService.buyMessage(id));
    }
}
