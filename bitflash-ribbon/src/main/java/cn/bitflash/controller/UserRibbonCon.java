
package cn.bitflash.controller;

import cn.bitflash.feign.UserRibbon;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
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
     * 公用接口
     *
     */

    @PostMapping("/user/withinCode/selectone")
    public UserInvitationCodeEntity selectOne(@RequestBody EntityWrapper entityWrapper){
        return  userRibbon.selectOne(entityWrapper);
    }

    @PostMapping("/user/withinInfo/insert")
    public boolean insert(@RequestBody UserInfoEntity userInfoEntity){
        return  userRibbon.insert(userInfoEntity);
    }

}

