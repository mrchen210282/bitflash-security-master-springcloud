package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.interceptor.ApiLoginInterceptor;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.TokenService;
import cn.bitflash.service.UserService;
import cn.bitflash.login.LoginForm;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.validator.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
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
    public R logout(@RequestAttribute(ApiLoginInterceptor.UID) String uid) {
        tokenService.expireToken(uid);
        return R.ok();
    }

    /**
     * 修改密码
     *
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @Login
    @PostMapping("changePassword")
    public R changePwd(@RequestAttribute(ApiLoginInterceptor.UID) String uid, @RequestParam String oldPwd, @RequestParam String newPwd) {
        UserEntity user = userService.selectOne(new EntityWrapper<UserEntity>().eq(ApiLoginInterceptor.UID,uid));
        if (oldPwd.equals(user.getPassword())) {
            user.setPassword(newPwd);
            userService.updateById(user);
            return R.ok();
        } else {
            return R.error("原密码不正确");
        }
    }

    /**
     * 修改密码
     *
     * @param mobile
     * @param newPwd
     * @return
     */
    @PostMapping("changePassword2")
    public R changePwd2(@RequestParam String mobile, @RequestParam String newPwd) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(newPwd);
        userEntity.setMobile(mobile);
        boolean rst = userService.updateById(userEntity);
        if (rst) {
            return R.ok();
        } else {
            return R.error("修改失败");
        }
    }

}
