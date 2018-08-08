package cn.bitflash.controller;

import cn.bitflash.annotation.*;
import cn.bitflash.service.*;
import cn.bitflash.user.*;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.utils.Common;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 获取账户信息
 *
 * @author eric
 */
@RestController
@RequestMapping("/api" )
//@Api(tags = "获取用户信息接口" )
public class ApiAccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private PlatFormConfigService platFormConfigService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserPayPwdService userPayPwdService;

    @Autowired
    private UserTradeHistoryService userTradeHistoryService;

    @Login
    @GetMapping("userInfo")
    //@ApiOperation(value = "获取用户信息", response = UserEntity.class)
    public R userInfo(@LoginUser UserEntity user) {
        return R.ok().put("user", user);
    }

    /**
     * 1.为判断成功 -1 为判断失败
     *
     * @author chen
     */
    @Login
    @GetMapping("accountInfo" )
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
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd" );
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            calendar.add(Calendar.DATE, -1);
                            Date calendarDate = calendar.getTime();

                            map.put("createTime", format.format(calendarDate));
                            map.put("purchaseUid", account.getUid());
                            //查询出昨日购买
                            Map<String, Object> returnMap = userTradeHistoryService.selectTradeHistoryIncome(map);
                            if (null != returnMap.get("quantity" )) {
                                yesterDayPurchase = returnMap.get("quantity" );
                            } else {
                                yesterDayPurchase = "0";
                            }

                            map.put("purchaseUid", account.getUid());
                            //查询出总购买
                            Map<String, Object> purchaseMap = userTradeHistoryService.selectTradeHistoryIncome(map);

                            if (null != purchaseMap.get("quantity" )) {
                                totalPurchase = purchaseMap.get("quantity" );
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
                    return R.error("该用户不存在！" );
                }
            } else {
                return R.error("该用户不存在！" );
            }
        } else {
            return R.error("该用户不存在！" );
        }
    }

    @Login
    @PostMapping("getInvitationCode" )
    //@ApiOperation(value = "获取推广码", response = UserInvitationCodeEntity.class)
    public R getInvitationcode(@UserInvitationCode UserInvitationCodeEntity userInvitationCode) {
        UserRelationEntity ur = userRelationService.selectOne(new EntityWrapper<UserRelationEntity>().eq("invitation_code", userInvitationCode.getLftCode()));
        Map<String, Object> map = new HashMap<String, Object>();
        String address = platFormConfigService.getVal(Common.ADDRESS);
        map.put("lftCode", userInvitationCode.getLftCode());
        map.put("address", address);
        if (ur != null) {
            map.put("rgtCode", userInvitationCode.getRgtCode());
        } else {
            map.put("rgtCode", "请先在左区排点" );
        }

        return R.ok().put("invitationCode", map);

    }

    @Login
    @PostMapping("getRelation" )
    //@ApiOperation(value = "获取用户体系", response = List.class)
    public R getRelation(@UserRelation List<UserRelationJoinAccountEntity> ura, @LoginUser UserEntity userEntity) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (ura != null) {
            map.put("lft_a", ura.get(0).getLftAchievement());
            map.put("rgt_a", ura.get(0).getRgtAchievement());
            if (StringUtils.isNotBlank(ura.get(0).getUid())) {
                UserInfoEntity userInfoEntity = userInfoService.selectById(userEntity.getUid());
                if (null != userInfoEntity) {
                    map.put("username", userInfoEntity.getNickname());
                }
            }
            map.put("c_lft_realname", "未排点" );
            map.put("c_rgt_realname", "未排点" );
            if (ura.size() > 1) {
                map.put("c_lft_realname", ura.get(1).getRealname());
                int cl_rgt = ura.get(1).getRgt();
                for (UserRelationJoinAccountEntity t : ura) {
                    if (t.getLft() == (cl_rgt + 1)) {
                        map.put("c_rgt_realname", t.getRealname());
                    }

                }
            }
        }
        return R.ok().put("myRelation", map);
    }

    @Login
    @PostMapping("changePassword" )
    //@ApiOperation(value = "修改密码", response = UserEntity.class)
    public R changePwd(@LoginUser UserEntity user, @RequestParam String oldPwd, @RequestParam String newPwd) {
        if (oldPwd.equals(user.getPassword())) {
            user.setPassword(newPwd);
            userService.update(user, new EntityWrapper<UserEntity>().eq("uid", user.getUid()));
            return R.ok();
        } else {
            return R.error("原密码不正确" );
        }
    }

    @PostMapping("changePassword2" )
   // @ApiOperation(value = "修改密码" )
    public R changePwd2(@RequestParam String mobile, @RequestParam String newPwd) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(newPwd);

        boolean rst = userService.update(userEntity, new EntityWrapper<UserEntity>().eq("mobile", mobile));
        if (rst) {
            return R.ok();
        } else {
            return R.error("修改失败" );
        }
    }

}
