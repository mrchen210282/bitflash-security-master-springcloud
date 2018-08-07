package cn.bitflash.controller;

import cn.bitflash.service.TokenService;
import cn.bitflash.service.UserService;
import cn.bitflash.user.LoginForm;
import cn.bitflash.utils.R;
import cn.bitflash.annotation.Login;
import common.validator.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user-app")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("login")
    public R login(@RequestBody LoginForm form){
        // 表单校验
        ValidatorUtils.validateEntity(form);
        // 用户登录
        Map<String, Object> map = userService.login(form);

        return R.ok(map);
    }

    @Login
    @PostMapping("logout" )
    public R logout(String uid) {
        System.out.println("login:"+uid);
        tokenService.expireToken(uid);
        return R.ok();
    }

}
