package cn.bitflash.controller;

import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.TokenService;
import cn.bitflash.service.UserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class ApiWithinLoginController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/withinToken/selectOne")
    public TokenEntity selectOneByToken(@RequestBody EntityWrapper<TokenEntity> wrapper){
        return this.tokenService.selectOne(wrapper);
    }

    @PostMapping("/withinUser/selectOne")
    public UserEntity selectOneByUser(@RequestBody EntityWrapper<UserEntity> entityWrapper){
        return userService.selectOne(entityWrapper);
    }

    @PostMapping("/withinUser/update")
    public boolean updateByUser(@RequestBody UserEntity userEntity,@RequestParam EntityWrapper<UserEntity> entityWrapper){
        return userService.update(userEntity,entityWrapper);
    }
}
