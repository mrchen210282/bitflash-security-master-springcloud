package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.annotation.UserAccount;
import cn.bitflash.annotation.UserInvitationCode;
import cn.bitflash.feignInterface.SysFeign;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.UserInfoService;
import cn.bitflash.service.UserRelationService;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import cn.bitflash.user.UserRelationEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.utils.Common;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private SysFeign sysfeign;


    /**
     * 获取用户体系信息
     *
     * @param userAccount
     * @param userInvitationCode
     * @return
     */
    @Login
    @PostMapping("getRelation")
    public R getRelation(@UserAccount UserAccountEntity userAccount, @UserInvitationCode UserInvitationCodeEntity userInvitationCode) {
        UserInfoEntity infoEntity = userInfoService.selectById(userAccount.getUid());
        if (Integer.valueOf(infoEntity.getIsVip()) < 0) {
            return R.error("没有加入社区");
        }
        //返回map
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
        UserRelationEntity ur = userRelationService.selectOne(new EntityWrapper<UserRelationEntity>()
                .eq("invitation_code", userInvitationCode.getLftCode()));
        String address = sysfeign.getVal(Common.ADDRESS);
        map.put("leftAddress", address + userInvitationCode.getLftCode());
        if (ur != null) {
            map.put("rightAddress", address + userInvitationCode.getRgtCode());
        } else {
            map.put("rightAddress", "");
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
    public R updateNickName(@RequestParam String nickname, @LoginUser UserEntity user) {
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
