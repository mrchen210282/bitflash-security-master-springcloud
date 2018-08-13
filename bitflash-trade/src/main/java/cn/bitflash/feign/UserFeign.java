package cn.bitflash.feign;

import cn.bitflash.feign.impl.UserFallback;
import cn.bitflash.login.UserEntity;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.user.UserRelationEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "bitflash-user",fallbackFactory = UserFallback.class)
public interface UserFeign {

    @PostMapping("/api/user/inner/selectUserById")
    public UserInfoEntity selectUserInfoById(@RequestParam("uid") String uid);

    @PostMapping("/api/user/inner/selectUserInfoList")
    public List<UserInfoEntity> selectUserInfoList(@RequestParam("params") Map<String, Object> params);

    @PostMapping("/api/user/inner/selectRelation")
    public UserRelationEntity selectRelation(@RequestParam("params") Map<String, Object> params);

    @PostMapping("/api/user/inner/selectUserPayPwd")
    public UserPayPwdEntity selectUserPayPwd(@RequestParam("params") Map<String, Object> params);

}
