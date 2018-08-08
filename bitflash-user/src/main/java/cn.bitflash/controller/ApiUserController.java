package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.annotation.PayPassword;
import cn.bitflash.service.UserPayPwdService;
import cn.bitflash.service.UserService;
import cn.bitflash.user.UserEntity;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chen
 */
@RestController
@RequestMapping("/api/user" )
//@Api(tags = "用户操作" )
public class ApiUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/selectOne")
    public List<UserEntity> selectOne(@RequestParam Map<String, Object> params){
        List<UserEntity> userList =  userService.selectByMap(params);
        return userList;
    }
}
