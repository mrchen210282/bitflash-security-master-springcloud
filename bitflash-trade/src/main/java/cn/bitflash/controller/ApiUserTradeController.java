package cn.bitflash.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.qos.logback.core.net.SyslogOutputStream;
import cn.bitflash.service.*;
import cn.bitflash.trade.*;
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
import cn.bitflash.feignInterface.UserFeign;
import cn.bitflash.login.UserEntity;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.user.UserPayUrlEntity;
import cn.bitflash.utils.BigDecimalUtils;
import cn.bitflash.utils.Common;
import cn.bitflash.utils.R;
import cn.bitflash.utils.RedisUtils;

/**
 * 获取用户交易接口
 *
 * @author wangjun
 * @version 2018年7月4日上午9:44:17
 */
@RestController
@RequestMapping("/api")
public class ApiUserTradeController {

    private final Logger logger = LoggerFactory.getLogger(ApiUserTradeController.class);

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserTradeHistoryService userTradeHistoryService;

    @Autowired
    private UserTradeService userTradeService;

    @Autowired
    private UserBrokerageService userBrokerageService;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserTradeConfigService userTradeConfigService;

    @Autowired
    private TradePoundageService tradePoundageService;

    /**
     * 卖出列表
     *
     * @param userAccount
     * @param pageNum     第几页
     * @return
     */
    @Login
    @PostMapping("/tradeList")
    public R tradeList(@UserAccount UserAccountEntity userAccount, @RequestParam String pageNum) {
        int pageTotal = 6;
        Map<String, Object> param = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(userAccount.getUid())) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("uid", userAccount.getUid());
            map.put("pageNum", new Integer(pageNum));
            map.put("pageTotal", new Integer(pageTotal));
            // 查询自身用户信息
            List<UserTradeEntity> listEntity = userTradeService.queryTrade(map);

            Integer count = userTradeService.selectTradeCount(map);

            param.put("availableAssets", BigDecimalUtils.DecimalFormat(userAccount.getAvailableAssets()));
            param.put("userAccountList", listEntity);
            param.put("totalRecord", count);
        } else {
            return R.error("无此用户！");
        }
        return R.ok().put("userAccount", param);
    }

    /**
     * 订单列表
     *
     * @param userAccount
     * @param pageNum     第几页
     * @return
     */
    @Login
    @PostMapping("/tradeList")
    public R orderList(@UserAccount UserAccountEntity userAccount, @RequestParam String pageNum) {
        int pageTotal = 6;
        Map<String, Object> param = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(userAccount.getUid())) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("uid", userAccount.getUid());
            map.put("pageNum", new Integer(pageNum));
            map.put("pageTotal", new Integer(pageTotal));
            // 查询自身用户信息
            List<UserTradeEntity> listEntity = userTradeService.queryTrade(map);

            Integer count = userTradeService.selectTradeCount(map);

            param.put("availableAssets", BigDecimalUtils.DecimalFormat(userAccount.getAvailableAssets()));
            param.put("userAccountList", listEntity);
            param.put("totalRecord", count);
        } else {
            return R.error("无此用户！");
        }
        return R.ok().put("userAccount", param);
    }

    /**
     * 跳转添加卖出记录
     *
     * @param userAccount
     * @return
     */
    @Login
    @PostMapping("responseTrade")
    public R responseTrade(@UserAccount UserAccountEntity userAccount) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("uid", userAccount.getUid());

        UserAccountEntity userAccountEntity = userAccountService.selectById(userAccount.getUid());
        Map<String, Object> returnMap = null;
        if (null != userAccountEntity) {
            returnMap = userTradeService.selectTrade(param);
            returnMap.put("availableAssets", BigDecimalUtils.DecimalFormat(userAccount.getAvailableAssets()));
        }
        return R.ok().put("userAccount", returnMap);
    }

    /**
     * 跳转到下单页
     *
     * @param id
     * @return
     */
    @Login
    @PostMapping("forwardPay")
    public R forwardPay(@RequestParam String id) {
        if (StringUtils.isNotBlank(id)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            logger.info("订单号:" + id);

            List<Map<String, Object>> list = userTradeService.selectTradeUrl(map);
            Map<String, Object> returnMap = null;
            if (null != list && list.size() > 0) {
                returnMap = list.get(0);
                String quantity = returnMap.get("quantity").toString();
                String price = returnMap.get("price").toString();
                //卖出数量
                double number = Double.parseDouble(quantity);
                //卖出价格
                double prix = Double.parseDouble(quantity);

                DecimalFormat df = new DecimalFormat("######0.00");
                //计算总价格
                double sum = number * prix;

                //查询手续费
                UserTradeConfigEntity userTradeConfigEntity = userTradeConfigService.selectById(Common.TRADE_CONFIG_ID);
                Integer poundage = 0;
                if(null != userTradeConfigEntity) {
                    poundage = (int) userTradeConfigEntity.getPoundage() * 100;
                }

                //userTradeConfigEntity.getPoundage() * 100;
                returnMap.put("sum",df.format(sum));
                //手续费
                returnMap.put("poundage",poundage);
                //returnMap.put("wechatUrl", returnMap.get("imgUrl"));
//                if (list.size() > 1) {
//                    Map<String, Object> returnMap2 = list.get(1);
//                    if (null != returnMap2.get("imgUrl")) {
//                        returnMap.put("payUrl", returnMap2.get("imgUrl"));
//                    }
//                }
            }
            return R.ok().put("payMap", returnMap);
        } else {
            return R.error("参数不能为空！");
        }
    }

    /**
     * 添加卖出记录
     *
     * @param userAccount
     * @param quantity
     * @param price
     * @return
     */
    @Login
    @PostMapping("saveTrade")
    @Transactional
    public R saveTrade(@UserAccount UserAccountEntity userAccount, @RequestParam String quantity, @RequestParam String price) {

        // 先校验出售数量是否大于已有数量
        BigDecimal total = userAccount.getAvailableAssets();
        logger.info("uid:" + userAccount.getUid() + ",卖出数量:" + quantity + ",价格:" + price);
        if (StringUtils.isNotBlank(price)) {

            BigDecimal priceB = new BigDecimal(price);
            BigDecimal minPrice = new BigDecimal(Common.MIN_PRICE);
            if (priceB.compareTo(minPrice) <= -1) {
                return R.error("最低价格为0.33!");
            }
            BigDecimal quantityB = new BigDecimal(quantity);

            // 卖出数量
            double quantityD = Double.parseDouble(quantity);
            //必须为100的整数倍
            if (quantityD % Common.MULTIPLE == 0) {

                UserTradeConfigEntity userTradeConfigEntity = userTradeConfigService.selectOne(new EntityWrapper<UserTradeConfigEntity>().eq("id", "1"));
                if (null != userTradeConfigEntity) {

                    float poundage = userTradeConfigEntity.getPoundage();
                    logger.info("poundage:" + poundage);
                    // 手续费 = 卖出数量 * 0.05
                    double percent = quantityD * poundage;

                    BigDecimal percentB = new BigDecimal(Math.floor(percent));
                    // 总卖出数量 = 卖出数量 + 手续费
                    BigDecimal purchase = quantityB.add(percentB);
                    // 等于1表示total大于percentB,可以交易
                    if (total.compareTo(purchase) == 1 || total.compareTo(purchase) == 0) {
                        // 手续费 = 总可用-百分比
//                        UserBrokerageEntity userBrokerageEntity = userBrokerageService.selectById("1");
//                        if (null != userBrokerageEntity) {
//                            BigDecimal sellBrokerage = userBrokerageEntity.getSellBrokerage();
//                            BigDecimal brokerage = sellBrokerage.add(percentB);
//                            userBrokerageEntity.setSellBrokerage(brokerage);
//                            userBrokerageService.insertOrUpdate(userBrokerageEntity);

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
                        int id = Common.randomUtil();
                        userTrade.setId(id);
                        userTrade.setPrice(new BigDecimal(price));
                        userTrade.setState(Common.STATE_SELL);
                        userTrade.setQuantity(new BigDecimal(quantity));
                        userTrade.setUid(userAccount.getUid());
                        userTrade.setCreateTime(new Date());
                        userTradeService.insertUserTrade(userTrade);

                        // 1.先扣除手续费，可用于撤消
                        TradePoundageEntity tradePoundageEntity = new TradePoundageEntity();
                        tradePoundageEntity.setUserTradeId(id);
                        tradePoundageEntity.setUid(userAccount.getUid());
                        tradePoundageEntity.setPoundage(percentB);
                        tradePoundageService.insert(tradePoundageEntity);

                        return R.ok();
//                        } else {
//                            R.error();
//                        }
                    } else {
                        return R.error().put("code", "1");
                    }
                }

            } else {
                return R.error("卖出数量必须为1000的倍数！");
            }
        } else {
            return R.error("参数不能为空！");
        }
        return R.ok();
    }

    /**
     * 查询卖出购买记录
     *
     * @param user
     * @param state
     * @return
     */
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

    /**
     * 撤消交易
     * 订单id
     *
     * @param id
     * @return
     */
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

                    TradePoundageEntity tradePoundageEntity = tradePoundageService.selectById(id);
                    if (null != tradePoundageEntity) {

                        // 撤消后要把卖出数量返回到user_account中
                        // 调节收入+卖出数量 + 手续费
                        BigDecimal regulateIncome = userAccountEntity.getRegulateIncome().add(userTradeBean.getQuantity().add(tradePoundageEntity.getPoundage()));
                        BigDecimal availableAssets = userAccountEntity.getRegulateRelease().add(regulateIncome);
                        userAccountEntity.setAvailableAssets(availableAssets);
                        userAccountEntity.setRegulateIncome(regulateIncome);
                        userAccountService.updateById(userAccountEntity);

                        // 更新状态为撤消
                        map.put("state", Common.STATE_CANCEL);
                        userTradeService.updateTrade(map);

                    } else {
                        logger.info("根据订单id:" + id + ",查询不到交易记录");
                        return R.error();
                    }
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

            redisUtils.delete(id);

            // 更新
            UserTradeEntity userTradeEntity = new UserTradeEntity();
            userTradeEntity.setId(Integer.parseInt(id));
            userTradeEntity.setState(Common.STATE_SELL);
            userTradeService.insertOrUpdate(userTradeEntity);
        }
        return R.ok();
    }

    /**
     * 购买
     *
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

                            TradePoundageEntity tradePoundageEntity = tradePoundageService.selectById(id);
                            if (null != tradePoundageEntity) {
                                // 计算手续费
                                // 手续费=卖出数量*0.05
                                UserBrokerageEntity userBrokerageEntity = userBrokerageService.selectById("1");
                                if (null != userBrokerageEntity) {
                                    BigDecimal multiplyB = userBrokerageEntity.getSellBrokerage().add(tradePoundageEntity.getPoundage());
                                    userBrokerageEntity.setSellBrokerage(multiplyB);
                                    userBrokerageService.updateById(userBrokerageEntity);

                                    BigDecimal regulateIncome = userAccount.getRegulateIncome().add(quantity);
                                    userAccount.setRegulateIncome(regulateIncome);
                                    BigDecimal availableAssets = userAccount.getAvailableAssets().add(quantity);
                                    userAccount.setAvailableAssets(availableAssets);
                                    userAccount.setUid(userTradeHistoryEntity.getPurchaseUid());
                                    userAccountService.updateUserAccountByParam(userAccount);

                                    // 添加购买记录
                                    UserTradeHistoryEntity userTradeHistory = new UserTradeHistoryEntity();
                                    userTradeHistory.setUserTradeId(userTradeBean.getId());
                                    userTradeHistory.setSellUid(user.getUid());
                                    userTradeHistory.setCreateTime(new Date());
                                    userTradeHistory.setSellQuantity(quantity);
                                    userTradeHistory.setState(Common.STATE_PAY);
                                    userTradeHistory.setPrice(userTradeBean.getPrice());
                                    userTradeHistory.setCreateTime(new Date());
                                    userTradeHistoryService.updateUserTradeHistory(userTradeHistory);
                                }
                            } else {
                                logger.info("");
                                return R.error();
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
     * 查看订单明细
     *
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

            List<UserPayUrlEntity> url = userFeign.selectUserPayUrl(uid);
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
     * 我已付款
     *
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
                map.put("state", Common.STATE_CONFIRM);
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
     * 验证订单是否被锁定
     *
     * @param id 订单id
     * @author chen
     */
    @Login
    @PostMapping("provingState")
    public R provingState(@RequestParam String id, @LoginUser UserEntity user) {
        String uid = user.getUid();
        String[] str = redisUtils.get(id, String[].class);
        if (str == null || str.length == 0) {
            return R.ok().put("code", 200);
        } else if (str[1].equals(uid)) {
            return R.ok().put("code", 400);
        }
        return R.error("订单已锁定");
    }

    /**
     * 锁定订单
     *
     * @param id 订单id
     * @author chen
     */
    @Login
    @PostMapping("addLock")
    public R addLock(@RequestParam String id, @LoginUser UserEntity user) throws ParseException {
        UserTradeEntity userTradeEntity = userTradeService.selectById(id);
        if (userTradeEntity.getState().equals(Common.STATE_CANCEL)) {
            return R.error(502, "订单已经被撤销,无法锁定");
        }
        String uid = user.getUid();
        String countKey = Common.COUNT_LOCK + uid;
        logger.debug("当前锁定订单的数量为：" + redisUtils.get(countKey));
        Integer count = redisUtils.get(countKey, Integer.class) == null ? 0 : redisUtils.get(countKey, Integer.class);
        if (count < 3) {
            String[] str = redisUtils.get(id, String[].class);
            if (str == null || str.length == 0) {
                str = new String[2];
                str[0] = id;
                str[1] = uid;
                //过期时间1小时
                redisUtils.set(id, str, 60 * 60);
                //当天时间凌晨23:59:59的秒数
                long tomorrow = LocalDateTime.now().withHour(23)
                        .withMinute(59)
                        .withSecond(59).toEpochSecond(ZoneOffset.of("+8"));
                //当前时间秒数
                long now = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
                //设置过期时间为当天剩余时间的秒数
                redisUtils.set(countKey, ++count, (tomorrow - now));
                userTradeEntity.setState(Common.STATE_LOCK);
                userTradeService.updateById(userTradeEntity);
                return R.ok().put("code", 200);
            } else if (str[1].equals(uid)) {
                return R.error(500, "订单被锁定,本人锁定");
            }
            return R.error(502, "订单已经被锁定");
        }
        return R.error(501, "当天锁定数量已到上限");

    }


    /**
     * 更新交易订单状态
     *
     * @author chen
     */
    @Login
    @PostMapping("updateTradeState")
    public R updateTradeState(@LoginUser UserEntity user) {
        List<UserTradeEntity> trades = userTradeService.getByState("5");
        trades.stream().forEach((t) -> {
            String[] str = redisUtils.get(t.getId().toString(), String[].class);
            if (str == null || str.length == 0) {
                if (t.getState().equals(Common.STATE_LOCK)) {
                    t.setState(Common.STATE_SELL);
                    userTradeService.updateById(t);
                }
            }
        });
        return R.ok();
    }

    /**
     * 查询买家信息
     *
     * @param id
     * @return
     */
    @Login
    @PostMapping("buyMessage")
    public R buyMessage(@RequestParam String id) {
        return R.ok().put("buyMessage", userTradeService.buyMessage(id));
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        String time = String.valueOf(System.currentTimeMillis());
//        String token = AESTokenUtil.setToken(time, "陈承毅");
//        System.out.println("加密数据"+token);
//        String token2=AESTokenUtil.getToken(time,token);
//        System.out.println("解密数据"+token2);
        //System.out.println(randomUtil());

        float a = 0.05f * 100;
        int b = (int) a;
        System.out.println(b);
    }

}
