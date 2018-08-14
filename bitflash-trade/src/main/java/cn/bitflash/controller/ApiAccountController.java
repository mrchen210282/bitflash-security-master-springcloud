package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.annotation.UserAccount;
import cn.bitflash.feignInterface.UserFeign;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.UserAccountService;
import cn.bitflash.service.UserTradeHistoryService;
import cn.bitflash.trade.UserAccountBean;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.utils.BigDecimalUtils;
import cn.bitflash.utils.Common;
import cn.bitflash.utils.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取账户信息
 *
 * @author eric
 */
@RestController
@RequestMapping("/api")
public class ApiAccountController {

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserTradeHistoryService userTradeHistoryService;


    /**
     * 1.为判断成功 -1 为判断失败
     *
     * @author chen
     */
    @Login
    @GetMapping("accountInfo")
    //@ApiOperation(value = "获取用户账户信息", response = UserAccountEntity.class)
    public R accountInfo(@UserAccount UserAccountEntity account, @LoginUser UserEntity user) {
        String uid = account.getUid();
        UserInfoEntity userInfoEntity = null;
        UserPayPwdEntity userPayPwdEntity = null;
        if (null != user) {
            if (StringUtils.isNotBlank(account.getUid())) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("uid", account.getUid());
                UserAccountBean userAccount = userAccountService.selectUserAccount(map);
                if (null != userAccount) {
                    account.setDailyIncome(userAccount.getDailyIncome());
                    String avaliableAssets = BigDecimalUtils.DecimalFormat(userAccount.getAvailableAssets());

                    // 昨日购买
                    Object yesterDayPurchase = null;
                    // 总购买
                    Object totalPurchase = null;
                    // 查询是否为VIP
                    UserInfoEntity userInfoBean = userFeign.selectUserInfoById(account.getUid());

                    if (null != userInfoBean) {
                        // VIP等级为0，则表示为非会员体系
                        if (Common.VIP_LEVEL_0.equals(userInfoBean.getIsVip())) {
                            // 计算非会员昨日购买和总购买
                            Date date = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            calendar.add(Calendar.DATE, -1);
                            Date calendarDate = calendar.getTime();

                            map.put("createTime", format.format(calendarDate));
                            map.put("purchaseUid", account.getUid());
                            // 查询出昨日购买
                            Map<String, Object> returnMap = userTradeHistoryService.selectTradeHistoryIncome(map);
                            if (null != returnMap.get("quantity")) {
                                yesterDayPurchase = returnMap.get("quantity");
                            } else {
                                yesterDayPurchase = "0";
                            }

                            map.put("purchaseUid", account.getUid());
                            // 查询出总购买
                            Map<String, Object> purchaseMap = userTradeHistoryService.selectTradeHistoryIncome(map);

                            if (null != purchaseMap.get("quantity")) {
                                totalPurchase = purchaseMap.get("quantity");
                            } else {
                                totalPurchase = "0";
                            }

                            map.put("createTime", format.format(calendarDate));
                        }
                    }
                    userInfoEntity = userFeign.selectUserInfoById(uid);
                    userPayPwdEntity = userFeign.selectUserPayPwd(new ModelMap("uid", uid));
                    return R.ok().put("account", account).put("userInfo", userInfoEntity).put("vip", userInfoEntity.getIsVip()).put("sys", userInfoEntity.getInvitation()).put("sfz", userInfoEntity.getIsAuthentication()).put("payPwd", userPayPwdEntity == null ? -1 : 1).put("uuid", user.getUuid())
                            .put("avaliableAssets", avaliableAssets)
                            .put("yesterDayIncome", yesterDayPurchase).put("totalPurchase", totalPurchase).put("totalIncome", BigDecimalUtils.DecimalFormat(account.getTotelIncome())).put("dailyIncome", BigDecimalUtils.DecimalFormat(account.getDailyIncome()))
                            .put("nicklock", userAccount.getNicklock());
                } else {
                    return R.error("该用户不存在！");
                }
            } else {
                return R.error("该用户不存在！");
            }
        } else {
            return R.error("该用户不存在！");
        }
    }

}
