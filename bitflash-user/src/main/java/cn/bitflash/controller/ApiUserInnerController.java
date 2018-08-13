package cn.bitflash.controller;

import cn.bitflash.login.UserEntity;
import cn.bitflash.service.UserInfoService;
import cn.bitflash.service.UserInvitationCodeService;
import cn.bitflash.service.UserRelationService;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import cn.bitflash.user.UserRelationEntity;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author chen
 */
@RestController
@RequestMapping("/api/user/inner")
public class ApiUserInnerController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserInvitationCodeService userInvitationCodeService;
    @Autowired
    private UserRelationService userRelationService;

    @PostMapping("/selectone")
    public UserInvitationCodeEntity selectOne(@RequestBody Map<String,Object> map) {
        return userInvitationCodeService.selectByMap(map).get(0);
    }

    @PostMapping("/insert")
    public boolean insert(@RequestBody UserInfoEntity userInfoEntity) {
        return userInfoService.insert(userInfoEntity);
    }


    @PostMapping("/selectUserInfoById")
    public UserInfoEntity selectUserInfoById(@RequestParam("uid") String uid){
        return userInfoService.selectById(uid);
    }

    @PostMapping("/selectOneByUserRelation")
    public UserRelationEntity selectOneByUserRelation(@RequestBody Map<String,Object> map){
        return userRelationService.selectByMap(map).get(0);
    }

}
