package cn.bitflash.controller;

import cn.bitflash.service.UserAccountService;
import cn.bitflash.service.UserService;
import cn.bitflash.user.UserAccountEntity;
import cn.bitflash.user.UserEntity;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chen
 */
@RestController
@RequestMapping("/api" )
//@Api(tags = "用户操作" )
public class ApiUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccountService userAccountService;

    @PostMapping("/user/selectOneByUser")
    public List<UserEntity> selectOneByUser(@RequestParam Map<String, Object> params){
        List<UserEntity> userList =  userService.selectByMap(params);
        return userList;
    }

    @PostMapping("/account/selectOneByAccount")
    public List<UserAccountEntity> selectOneByAccount(@RequestParam Map<String, Object> params){
        List<UserAccountEntity> accountList =  userAccountService.selectByMap(params);
        return accountList;
    }

    @PostMapping("/account/updateByAccount")
    public void updateByAccount(UserAccountEntity useraccount,@RequestParam Map<String, Object> params){
        String key = null;
        Object value = null;
        for(String keys : params.keySet()){
            value = params.get(keys);
            key = keys;
        }
        userAccountService.update(useraccount,new EntityWrapper<UserAccountEntity>().eq(key,value));
    }


}
