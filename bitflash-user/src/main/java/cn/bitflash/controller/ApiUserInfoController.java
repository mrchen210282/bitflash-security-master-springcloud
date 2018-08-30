package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.UserInvitationCode;
import cn.bitflash.interceptor.ApiLoginInterceptor;
import cn.bitflash.login.UserEntity;
import cn.bitflash.loginutil.LoginUtils;
import cn.bitflash.service.UserInfoService;
import cn.bitflash.service.UserPayPwdService;
import cn.bitflash.service.UserPayUrlService;
import cn.bitflash.service.UserRelationService;
import cn.bitflash.sysutils.SysUtils;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.tradeutil.TradeUtils;
import cn.bitflash.user.*;
import cn.bitflash.utils.Common;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取账户信息
 *
 * @author eric
 */
@RestController
@RequestMapping("/api")
public class ApiUserInfoController {

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SysUtils sysfeign;

    @Autowired
    private LoginUtils loginUtils;

    @Autowired
    private UserPayPwdService userPayPwdService;

    @Autowired
    private UserPayUrlService userPayUrlService;

    @Autowired
    private TradeUtils tradeUtils;


    /**
     * 获取用户体系信息
     *
     * @param userInvitationCode
     * @return
     */
    @Login
    @PostMapping("getRelation")
    public R getRelation(@RequestAttribute("uid") String uid, @UserInvitationCode UserInvitationCodeEntity userInvitationCode) {
        UserAccountEntity userAccount = tradeUtils.selectOne(new ModelMap(ApiLoginInterceptor.UID, uid));
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
                leftRate = (((Math.round(left / (left + right))*0.6)+0.1) * 100) + "%";
            }
            String rightRate = "10%";
            if (right != 0) {
                rightRate = (((Math.round(right / (left + right))*0.6)+0.1) * 100) + "%";
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
     * @param nickname
     * @return
     */
    @Login
    @PostMapping("updateNickName")
    public R updateNickName(@RequestParam String nickname, @RequestAttribute("uid") String uid) {
        if (StringUtils.isNotBlank(nickname)) {
            if (nickname.length() <= 6) {
                UserInfoEntity userInfoEntity = userInfoService.selectOne(new EntityWrapper<UserInfoEntity>().eq("nickname", nickname));

                if (null != userInfoEntity && "1".equals(userInfoEntity.getNicklock())) {
                    return R.error("昵称不能修改！");
                } else {
                    UserInfoEntity userInfoBean = new UserInfoEntity();
                    userInfoBean.setNickname(nickname);
                    userInfoBean.setUid(uid);
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

    /**
     * 获取用户信息判断
     *
     * @param uid
     * @return
     */
    @Login
    @PostMapping("getUserPower")
    public R getUserPower(@RequestAttribute(ApiLoginInterceptor.UID) String uid) {
        UserInfoEntity infoEntity = userInfoService.selectById(uid);
        Map<String, Object> map = new HashMap<>();
        //是否是vip
        map.put("isVip", infoEntity.getIsVip());
        //是否是实名认证的
        map.put("isAuthentication", infoEntity.getIsAuthentication());
        //是否是体系内的
        map.put("isInvitation", infoEntity.getIsInvitation());
        //用户昵称
        map.put("nickName", infoEntity.getNickname());
        //是否修改过昵称
        map.put("nicklock", infoEntity.getNicklock());
        return R.ok(map);
    }

    /**
     * 获取钱包地址
     *
     * @param
     * @return
     */
    @Login
    @PostMapping("getWalletToken")
    public R getWalletToken(@RequestAttribute(ApiLoginInterceptor.UID) String uid) {
        UserEntity userEntity = loginUtils.selectOneByUser(new ModelMap("uid", uid));
        return R.ok(userEntity.getUuid());
    }

    /**
     * 验证用户是否设置交易密码,上传收款码
     *
     * @param uid
     * @return
     */
    @Login
    @PostMapping("validatePwd")
    public R validatePwd(@RequestAttribute(ApiLoginInterceptor.UID) String uid,@RequestParam(value="key",required = false)String key) {
        if(key==null){
            UserInfoEntity infoEntity = userInfoService.selectById(uid);
            //未进行实名认证
            if (infoEntity.getIsAuthentication().equals("0") || infoEntity.getIsAuthentication().equals("-1")) {
                return R.ok().put("msg", "3");
            }
            //实名认证中
            if (infoEntity.getIsAuthentication().equals("1")) {
                return R.ok().put("msg", "4");
            }
        }
        UserPayPwdEntity payPwdEntity = userPayPwdService.selectOne(new EntityWrapper<UserPayPwdEntity>().eq("uid", uid));
        if (payPwdEntity == null || StringUtil.isNullOrEmpty(payPwdEntity.getPayPassword())) {
            //如果没有交易密码
            return R.ok().put("msg", "1");
        }
        List<UserPayUrlEntity> urlEntity = userPayUrlService.selectList(new EntityWrapper<UserPayUrlEntity>().eq("uid", uid));
        if (urlEntity.size() < 3) {
            List<UserPayUrlEntity> userPayUrlEntity = urlEntity.stream()
                    .filter(u->u.getImgType().equals("1") || u.getImgType().equals("2") || u.getImgType().equals("5"))
                    .collect(Collectors.toList());
            if(userPayUrlEntity ==null || userPayUrlEntity.size()<1){
                //未上传收款码
                return R.ok().put("msg", "2");
            }
        }

        return R.ok().put("msg", "0");
    }
}
