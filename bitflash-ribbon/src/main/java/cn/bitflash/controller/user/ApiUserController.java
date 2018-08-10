package cn.bitflash.controller.user;

import cn.bitflash.feignInterface.BitflashUserFeign;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiUserController {

    @Autowired
    private BitflashUserFeign userFeign;

    /**
     * ApiAccountController
     * @return
     */

    @GetMapping("/userInfo")
    public R userInfo(){
        return userFeign.userInfo();
    }

    @GetMapping("/accountInfo" )
    public R accountInfo(){
        return userFeign.accountInfo();
    }

    @PostMapping("/getInvitationCode" )
    public R getInvitationcode(){
        return userFeign.getInvitationcode();
    }

    @PostMapping("/getRelation" )
    public R getRelation(){
        return userFeign.getRelation();
    }

    @PostMapping("/changePassword" )
    public R changePwd(@RequestParam("oldPwd") String oldPwd, @RequestParam("newPwd") String newPwd){
        return userFeign.changePwd(oldPwd,newPwd);
    }

    @PostMapping("/changePassword2" )
    public R changePwd2(@RequestParam("mobile") String mobile, @RequestParam("newPwd") String newPwd){
        return userFeign.changePwd2(mobile,newPwd);
    }

    @PostMapping("/updateNickName")
    public R updateNickName(@RequestParam("nickname") String nickname){
        return userFeign.updateNickName(nickname);
    }



    /**
     * ApiUserPayPwdController
     */

    @PostMapping("/payPwd/addPayPwd" )
    public R addPayPassword(@RequestParam("payPassword") String payPassword){
        return userFeign.addPayPassword(payPassword);
    }

    @PostMapping("/payPwd/updatePayPwd" )
    public R updatePayPwd(@RequestParam("oldPwd") String oldPwd, @RequestParam("newPwd") String newPwd){
        return userFeign.updatePayPwd(oldPwd,newPwd);
    }



    /**
     * ApiUserPayUrlController
     */

    @PostMapping("/upload" )
    public R upload(@RequestParam("img") String img, @RequestParam("imgType") String imgType){
        return userFeign.upload(img,imgType);
    }

    @PostMapping("/userInfoImg" )
    public R userInfoImg(){
        return userFeign.userInfoImg();
    }

    @PostMapping("/uploadImg" )
    public R uploadImgMessage(@RequestParam("img") String img, @RequestParam("imgType") String imgType){
        return userFeign.uploadImgMessage(img,imgType);
    }

    @PostMapping("/getPictureUrl" )
    public R getSFZAdress(@RequestParam("imgType") String imgType){
        return userFeign.getSFZAdress(imgType);
    }



    /**
     * ApiVipLevelController
     */

    @PostMapping("/vip/getVipLevel" )
    public R getVipLevel(){
        return userFeign.getVipLevel();
    }

    @PostMapping("/vip/updateVipLevel" )
    public R updateVipLevel(){
        return userFeign.updateVipLevel();
    }



    /**
     * ApiUserController
     */

    @PostMapping("/user/withinCode/selectone")
    public UserInvitationCodeEntity selectOne(@RequestBody EntityWrapper entityWrapper){
        return userFeign.selectOne(entityWrapper);
    }

    @PostMapping("/user/withinInfo/insert")
    public boolean insert(@RequestBody UserInfoEntity userInfoEntity){
        return userFeign.insert(userInfoEntity);
    }


}
