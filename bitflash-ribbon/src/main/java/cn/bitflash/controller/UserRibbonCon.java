package cn.bitflash.controller;

import cn.bitflash.feignInterface.UserRibbon;
import cn.bitflash.login.UserEntity;
import cn.bitflash.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserRibbonCon {

    @Autowired
    private UserRibbon userRibbon;
/**
     *获取账户信息
     * @return
     */
    @GetMapping("/userInfo")
    public R userInfo(){
        return userRibbon.userInfo();
    }

    @GetMapping("/accountInfo")
    public R accountInfo(){
        return userRibbon.accountInfo();
    }

    @PostMapping("/getInvitationCode")
    public R getInvitationcode(){
        return userRibbon.getInvitationcode();
    }

    @PostMapping("/getRelation")
    public R getRelation(){
        return userRibbon.getRelation();
    }

    @PostMapping("/changePassword")
    public R changePwd(@RequestParam String oldPwd, @RequestParam String newPwd){
        return userRibbon.changePwd(oldPwd,newPwd);
    }

    @PostMapping("/changePassword2")
    public R changePwd2(@RequestParam String mobile, @RequestParam String newPwd){
        return userRibbon.changePwd2(mobile,newPwd);
    }



/**
     * 用户交易密码
     */


    @PostMapping("/payPwd/addPayPwd")
    public R addPayPassword(@RequestParam String payPassword){
        return userRibbon.addPayPassword(payPassword);
    }

    @PostMapping("/payPwd/updatePayPwd")
    public R updatePayPwd(@RequestParam String oldPwd, @RequestParam String newPwd){
        return userRibbon.updatePayPwd(oldPwd,newPwd);
    }


/**
     * 获取用户vip信息
     */

    @PostMapping("/vip/getVipLevel")
    public R getVipLevel(){
        return userRibbon.getVipLevel();
    }

    @PostMapping("/vip/updateVipLevel")
    public R updateVipLevel(){
        return userRibbon.updateVipLevel();
    }


/**
     * 用户操作
     */

    @GetMapping("/user/selectOne")
    public List<UserEntity> selectOne(@RequestParam Map<String, Object> params){
        return userRibbon.selectOne(params);
    }

}

