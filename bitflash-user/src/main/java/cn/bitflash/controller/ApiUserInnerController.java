package cn.bitflash.controller;

import cn.bitflash.service.UserInfoService;
import cn.bitflash.service.UserInvitationCodeService;
import cn.bitflash.service.UserPayPwdService;
import cn.bitflash.service.UserRelationService;
import cn.bitflash.user.*;
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
    @Autowired
    private UserPayPwdService userPayPwdService;


    @PostMapping("/selectone")
    public UserInvitationCodeEntity selectOne(@RequestParam("invitationCode") String invitationCode) {
        return userInvitationCodeService.selectOne(new EntityWrapper<UserInvitationCodeEntity>().eq("lft_code",invitationCode).or().eq("rgt_code",invitationCode));
    }

    @PostMapping("/insert")
    public boolean insert(@RequestBody UserInfoEntity userInfoEntity) {
        return userInfoService.insert(userInfoEntity);
    }

    @PostMapping("/selectUserInfoById")
    public UserInfoEntity selectUserInfoById(@RequestParam("uid") String uid){
        return userInfoService.selectById(uid);
    }

    @PostMapping("/selectUserRelation")
    public UserRelationEntity selectUserRelation(@RequestBody Map<String,Object> map){
        return userRelationService.selectByMap(map).get(0);
    }

    @PostMapping("/selectUserPayPwd")
    public UserPayPwdEntity selectUserPayPwd(@RequestBody Map<String,Object> map){
        return userPayPwdService.selectByMap(map).get(0);
    }

    @PostMapping("/selectUserInfoList")
    public List<UserInfoEntity> selectUserInfoList(@RequestBody Map<String,Object> map){
        return userInfoService.selectByMap(map);
    }

    @PostMapping("/selectRelation")
    public UserInvitationCodeEntity selectRelation(@RequestParam("uid") String uid) {
        return userInvitationCodeService.selectOne(new EntityWrapper<UserInvitationCodeEntity>().eq("uid",uid));
    }

    @PostMapping("/selectTreeNodes")
    public List<UserRelationJoinAccountEntity> selectTreeNodes(@RequestParam("uid") String uid) {
        return userRelationService.selectTreeNodes(uid);
    }
}
