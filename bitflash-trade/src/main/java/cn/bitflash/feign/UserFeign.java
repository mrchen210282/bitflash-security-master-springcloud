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

    @PostMapping("/api/user/selectUserOne")
    public List<UserEntity> selectUserOne(@RequestParam("params") Map<String, Object> params);

    @PostMapping("/api/user/selectUserById")
    public UserInfoEntity selectUserById(@RequestParam("uid") String uid);


    @PostMapping("/api/user/selectUserInfoOne")
    public List<UserInfoEntity> selectUserInfoOne(@RequestParam("params") Map<String, Object> params);

    @PostMapping("/api/user/selectUserInfoById")
    public UserInfoEntity selectUserInfoById(@RequestParam("uid") String uid);

    @PostMapping("/api/user/selectRelationOne")
    public UserRelationEntity selectRelationOne(@RequestParam("params") Map<String, Object> params);

    @PostMapping("/api/user/selectPayPwdOne")
    public UserPayPwdEntity selectPayPwdOne(@RequestParam("params") Map<String, Object> params);

}
