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
@RequestMapping("/api/login/inner")
public class LoginInnerController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserGTCidService userGTCidService;

    @PostMapping("selectToken")
    public TokenEntity selectToken(@RequestBody Map<String,Object> map){
        List<TokenEntity> list=this.tokenService.selectByMap(map);
        if(list.size()>0){
            return list.get(0);
        }
        return null;

    }

    @PostMapping("selectUser")
    public UserEntity selectUser(@RequestBody Map<String,Object> map){
        List<UserEntity> list= userService.selectByMap(map);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @PostMapping("updateByUser")
    public boolean updateByUser(@RequestBody UserEntity userEntity){
        //----
        return userService.updateById(userEntity);
    }

    @PostMapping("selectGT")
    public UserGTCidEntity selectGT(@RequestBody Map<String,Object> map){
        List<UserGTCidEntity> list= userGTCidService.selectByMap(map);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }
}
