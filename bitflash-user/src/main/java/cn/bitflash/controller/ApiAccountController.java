package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.annotation.UserAccount;
import cn.bitflash.annotation.UserInvitationCode;
import cn.bitflash.feign.LoginFeign;
import cn.bitflash.feign.SysFeign;
import cn.bitflash.feign.TradeFeign;
import cn.bitflash.login.UserEntity;
import cn.bitflash.redisConfig.RedisKey;
import cn.bitflash.service.UserInfoService;
import cn.bitflash.service.UserPayPwdService;
import cn.bitflash.service.UserRelationService;
import cn.bitflash.trade.UserAccountBean;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.user.UserRelationEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.utils.Common;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
//@Api(tags = "获取用户信息接口" )
public class ApiAccountController {

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserPayPwdService userPayPwdService;

    @Autowired
    private TradeFeign tradeFeign;

    @Autowired
    private SysFeign sysfeign;

    @Autowired
    private LoginFeign loginFeign;

    @Login
    @GetMapping("userInfo")
    public R userInfo(@LoginUser UserEntity user) {
        return R.ok().put("user", user);
    }

    /**
     * 登录后获取用户信息
     *
     * @param account
     * @param user
     * @return
     */
    @Login
    @GetMapping("accountInfo")
    public R accountInfo(@UserAccount UserAccountEntity account, @LoginUser UserEntity user) {
        String uid = account.getUid();
        UserInfoEntity userInfoEntity = null;
        UserPayPwdEntity userPayPwdEntity = null;
        if (null != user) {
            if (StringUtils.isNotBlank(account.getUid())) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("uid", account.getUid());
                UserAccountBean userAccount = tradeFeign.selectUserAccount(map);
                if (null != userAccount) {
                    account.setDailyIncome(userAccount.getDailyIncome());

                    //昨日购买
                    Object yesterDayPurchase = null;
                    //总购买
                    Object totalPurchase = null;
                    //查询是否为VIP
                    UserInfoEntity userInfoBean = userInfoService.selectById(account.getUid());
                    if (null != userInfoBean) {
                        //VIP等级为0，则表示为非会员体系
                        if (Common.VIP_LEVEL_0.equals(userInfoBean.getIsVip())) {
                            //计算非会员昨日购买和总购买
                            Date date = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            calendar.add(Calendar.DATE, -1);
                            Date calendarDate = calendar.getTime();

                            map.put("createTime", format.format(calendarDate));
                            map.put("purchaseUid", account.getUid());
                            //查询出昨日购买
                            Map<String, Object> returnMap = tradeFeign.selectTradeHistoryIncome(map);
                            if (null != returnMap.get("quantity")) {
                                yesterDayPurchase = returnMap.get("quantity");
                            } else {
                                yesterDayPurchase = "0";
                            }

                            map.put("purchaseUid", account.getUid());
                            //查询出总购买
                            Map<String, Object> purchaseMap = tradeFeign.selectTradeHistoryIncome(map);

                            if (null != purchaseMap.get("quantity")) {
                                totalPurchase = purchaseMap.get("quantity");
                            } else {
                                totalPurchase = "0";
                            }

                            map.put("createTime", format.format(calendarDate));
                        }
                    }
                    userInfoEntity = userInfoService.selectOne(new EntityWrapper<UserInfoEntity>().eq("uid", uid));
                    userPayPwdEntity = userPayPwdService.selectOne(new EntityWrapper<UserPayPwdEntity>().eq("uid", uid));
                    return R.ok().put("account", account).put("userInfo", userInfoEntity).put("vip", userInfoEntity.getIsVip())
                            .put("sys", userInfoEntity.getInvitation()).put("sfz", userInfoEntity.getIsAuthentication())
                            .put("payPwd", userPayPwdEntity == null ? -1 : 1).put("uuid", user.getUuid())
                            .put("yesterDayIncome", yesterDayPurchase).put("totalPurchase", totalPurchase)
                            .put("totalIncome", account.getTotelIncome()).put("dailyIncome", account.getDailyIncome())
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

    /**
     * 获取推广码
     *
     * @param userInvitationCode
     * @return
     */
    @Login
    @PostMapping("getInvitationCode")
    public R getInvitationcode(@UserInvitationCode UserInvitationCodeEntity userInvitationCode) {
        UserRelationEntity ur = userRelationService.selectOne(new EntityWrapper<UserRelationEntity>().eq("invitation_code", userInvitationCode.getLftCode()));
        Map<String, Object> map = new HashMap<String, Object>();
        String address = sysfeign.getVal(Common.ADDRESS);
        map.put("lftCode", userInvitationCode.getLftCode());
        map.put("address", address);
        if (ur != null) {
            map.put("rgtCode", userInvitationCode.getRgtCode());
        } else {
            map.put("rgtCode", "请先在左区排点");
        }

        return R.ok().put("invitationCode", map);

    }

    /**
     * 获取用户体系
     * @param userEntity
     * @return
     */
    @Login
    @PostMapping("getRelation")
    public R getRelation(@UserAccount UserAccountEntity userAccount, @LoginUser UserEntity userEntity) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (userAccount != null) {
            Double left = userAccount.getLftAchievement().doubleValue();
            Double right = userAccount.getRgtAchievement().doubleValue();
            String leftRate = "10%";
            if (left != 0) {
                leftRate = (Math.round(left / (left + right))) * 100 + "%";
            }
            String rightRate = "10%";
            if (right != 0) {
                rightRate = (Math.round(right / (left + right))) * 100 + "%";
            }
            map.put("leftRate", leftRate);
            map.put("rightRate", rightRate);
            map.put("lft_a", left);
            map.put("rgt_a", right);
        }
        return R.ok().put("myRelation", map);
    }

    /**
     * 修改密码
     *
     * @param user
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @Login
    @PostMapping("changePassword")
    public R changePwd(@LoginUser UserEntity user, @RequestParam String oldPwd, @RequestParam String newPwd) {
        if (oldPwd.equals(user.getPassword())) {
            user.setPassword(newPwd);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("uid",user.getUid());
            loginFeign.update(user, map);
            return R.ok();
        } else {
            return R.error("原密码不正确");
        }
    }

    /**
     * 修改密码
     *
     * @param mobile
     * @param newPwd
     * @return
     */
    @PostMapping("changePassword2")
    public R changePwd2(@RequestParam String mobile, @RequestParam String newPwd) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(newPwd);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("mobile",mobile);
        boolean rst = loginFeign.update(userEntity, map);
        if (rst) {
            return R.ok();
        } else {
            return R.error("修改失败");
        }
    }

    /**
     * 修改昵称
     *
     * @param user
     * @param nickname
     * @return
     */
    @Login //@LoginUser UserEntity user,
    @PostMapping("updateNickName")
    public R updateNickName(@RequestParam String nickname, HttpServletRequest request) {
        System.out.println(request.getSession().getAttribute(RedisKey.MOBILE.toString()));
        /*if (StringUtils.isNotBlank(nickname)) {
            if (nickname.length() <= 6) {
                UserInfoEntity userInfoEntity = userInfoService.selectOne(new EntityWrapper<UserInfoEntity>().eq("nickname", nickname));

                if (null != userInfoEntity && "1".equals(userInfoEntity.getNicklock())) {
                    return R.error("昵称不能修改！");
                } else {
                    UserInfoEntity userInfoBean = new UserInfoEntity();
                    userInfoBean.setNickname(nickname);
                    userInfoBean.setUid(user.getUid());
                    userInfoBean.setNicklock("1");
                    userInfoService.updateById(userInfoBean);
                    return R.ok();
                }
            } else {
                return R.error("昵称长度不能大于6个字！");
            }
        } else {
            return R.error("昵称不能为空！");
        }*/
        return R.error("昵称不能为空！");
    }

}
