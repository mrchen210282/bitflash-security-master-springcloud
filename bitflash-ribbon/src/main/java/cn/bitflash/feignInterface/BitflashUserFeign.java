package cn.bitflash.feignInterface;


import cn.bitflash.feignInterface.impl.UserFeignFallback;
import cn.bitflash.login.UserEntity;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.user.UserRelationJoinAccountEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "birflash-user",fallbackFactory = UserFeignFallback.class)
public interface BitflashUserFeign {

    /**
     * ApiAccountController
     * @return
     */

    @GetMapping("/api/userInfo")
    R userInfo();

    @GetMapping("/api/accountInfo" )
    R accountInfo();

    @PostMapping("/api/getInvitationCode" )
    R getInvitationcode();

    @PostMapping("/api/getRelation" )
    R getRelation();

    @PostMapping("/api/changePassword" )
    R changePwd(@RequestParam("oldPwd") String oldPwd, @RequestParam("newPwd") String newPwd);

    @PostMapping("/api/changePassword2" )
    R changePwd2(@RequestParam("mobile") String mobile, @RequestParam("newPwd") String newPwd);

    @PostMapping("/api/updateNickName")
    R updateNickName(@RequestParam("nickname") String nickname);



    /**
     * ApiUserPayPwdController
     */

    @PostMapping("/api/payPwd/addPayPwd" )
    R addPayPassword(@RequestParam("payPassword") String payPassword);

    @PostMapping("/api/payPwd/updatePayPwd" )
    R updatePayPwd(@RequestParam("oldPwd") String oldPwd, @RequestParam("newPwd") String newPwd);



    /**
     * ApiUserPayUrlController
     */

    @PostMapping("/api/upload" )
    R upload(@RequestParam("img") String img, @RequestParam("imgType") String imgType);

    @PostMapping("/api/userInfoImg" )
    R userInfoImg();

    @PostMapping("/api/uploadImg" )
    R uploadImgMessage(@RequestParam("img") String img, @RequestParam("imgType") String imgType);

    @PostMapping("/api/getPictureUrl" )
    R getSFZAdress(@RequestParam("imgType") String imgType);



    /**
     * ApiVipLevelController
     */

    @PostMapping("/api/vip/getVipLevel" )
    R getVipLevel();

    @PostMapping("/api/vip/updateVipLevel" )
    R updateVipLevel();



    /**
     * ApiUserController
     */

    @PostMapping("/api/user/withinCode/selectone")
    UserInvitationCodeEntity selectOne(@RequestBody EntityWrapper entityWrapper);

    @PostMapping("/api/user/withinInfo/insert")
    boolean insert(@RequestBody UserInfoEntity userInfoEntity);

}
