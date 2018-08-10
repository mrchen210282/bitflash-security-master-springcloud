package cn.bitflash.controller;

import cn.bitflash.service.UserInfoService;
import cn.bitflash.service.UserInvitationCodeService;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author chen
 */
@RestController
@RequestMapping("/api" )
//@Api(tags = "用户操作" )
public class ApiUserController {

    @Autowired
    private UserInvitationCodeService userInvitationCodeService;

    @Autowired
    private UserInfoService userInfoService;


    @PostMapping("/user/withinCode/selectone")
    public UserInvitationCodeEntity selectOne(@RequestBody EntityWrapper entityWrapper){
        return  userInvitationCodeService.selectOne(entityWrapper);
    }

    @PostMapping("/user/withinInfo/insert")
    public boolean insert(@RequestBody UserInfoEntity userInfoEntity){
        return  userInfoService.insert(userInfoEntity);
    }

}
