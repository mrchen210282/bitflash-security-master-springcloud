package cn.bitflash.userutil;

import cn.bitflash.user.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Service
@FeignClient(name = "bitflash-user",fallbackFactory = UserFallback.class)
@Component
public interface UserUtils {

    @PostMapping("/api/user/inner/selectUserInfoById")
    public UserInfoEntity selectUserInfoById(@RequestParam("uid") String uid);

    @PostMapping("/api/user/inner/selectUserInfoList")
    public List<UserInfoEntity> selectUserInfoList(@RequestBody Map<String, Object> params);

    @PostMapping("/api/user/inner/selectUserRelation")
    public UserRelationEntity selectRelation(@RequestParam("params") Map<String, Object> params);

    @PostMapping("/api/user/inner/selectUserPayPwd")
    public UserPayPwdEntity selectUserPayPwd(@RequestBody Map<String, Object> params);

    @PostMapping("/api/user/inner/selectTreeNodes")
    public List<UserRelationJoinAccountEntity> selectTreeNodes(@RequestParam("uid") String uid);

    @PostMapping("/api/user/inner/selectUserInvitationCode")
    public UserInvitationCodeEntity selectUserInvitationCode(@RequestParam("uid") String uid);

    @PostMapping("/api/user/inner//selectUserPayUrl")
    public List<UserPayUrlEntity> selectUserPayUrl(@RequestParam("uid")String uid);
}
