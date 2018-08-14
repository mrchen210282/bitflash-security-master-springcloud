package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.annotation.UserAccount;
import cn.bitflash.annotation.UserInvitationCode;
import cn.bitflash.feign.LoginFeign;
import cn.bitflash.feign.SysFeign;
import cn.bitflash.feign.TradeFeign;
import cn.bitflash.login.UserEntity;
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
public class ApiUserInfoController {

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

    /**
     * 用户信息
     * @param user
     * @return
     */
    @Login
    @GetMapping("userInfo")
    public R userInfo(@LoginUser UserEntity user) {
        return R.ok().put("user", user);
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
     * 修改昵称
     *
     * @param user
     * @param nickname
     * @return
     */
    @Login
    @PostMapping("updateNickName")
    public R updateNickName(@RequestParam String nickname,@LoginUser UserEntity user) {
        if (StringUtils.isNotBlank(nickname)) {
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
        }
    }

}
