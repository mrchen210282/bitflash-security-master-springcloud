package cn.bitflash.controller;

import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserEntity;
import cn.bitflash.login.UserGTCidEntity;
import cn.bitflash.service.TokenService;
import cn.bitflash.service.UserGTCidService;
import cn.bitflash.service.UserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class ApiWithinLoginController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserGTCidService userGTCidService;

    @PostMapping("/withinToken/selectOne")
    public TokenEntity selectOneByToken(@RequestBody Map<String,Object> map){
        List<TokenEntity> list=this.tokenService.selectByMap(map);
        if(list.size()>0){
            return list.get(0);
        }
        return null;

    }

    @PostMapping("/withinUser/selectOne")
    public UserEntity selectOneByUser(@RequestBody Map<String,Object> map){
        return userService.selectByMap(map).get(0);
    }

    @PostMapping("/withinUser/update")
    public boolean updateByUser(@RequestBody UserEntity userEntity,@RequestParam EntityWrapper<UserEntity> entityWrapper){
        return userService.update(userEntity,entityWrapper);
    }

    @PostMapping("/wthinGT/selectOne")
    public UserGTCidEntity selectOneByGT(@RequestBody Map<String,Object> map){
        return userGTCidService.selectByMap(map).get(0);
    }
}
