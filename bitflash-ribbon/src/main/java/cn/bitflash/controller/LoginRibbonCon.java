package cn.bitflash.controller;

import cn.bitflash.feign.LoginRibbon;
import cn.bitflash.user.LoginForm;
import cn.bitflash.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/login")
public class LoginRibbonCon {

    @Autowired
    private LoginRibbon loginRibbon;

    @PostMapping("/login")
    public R login(@RequestBody LoginForm form) {
        return loginRibbon.login(form);
    }

    @PostMapping("logout")
    public R logout(@RequestParam String uid,HttpServletRequest request) {
        System.out.println("ribbon:"+uid);
        loginRibbon.logout(uid);
        return R.error();
    }
}
