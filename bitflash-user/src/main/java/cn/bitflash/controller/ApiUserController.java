package cn.bitflash.controller;

import cn.bitflash.service.UserService;
import cn.bitflash.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
