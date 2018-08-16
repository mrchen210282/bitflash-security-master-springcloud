package cn.bitflash.feignInterface;

import cn.bitflash.feignInterface.impl.LoginFallback;
import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserEntity;
import cn.bitflash.login.UserGTCidEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "bitflash-login",fallbackFactory = LoginFallback.class)
public interface LoginFeign {

    @PostMapping("/api/user/inner/selectUserOne")
    public List<UserEntity> selectUserOne(@RequestParam("params") Map<String, Object> params);


    @PostMapping("/api/login/inner/selectToken")
    TokenEntity selectOne(@RequestBody Map<String,Object> map);

    @PostMapping("/api/login/inner/selectUser")
    UserEntity selectOneByUser(@RequestBody Map<String,Object> map);

    @PostMapping("selectGT")
    public UserGTCidEntity selectGT(@RequestBody Map<String,Object> map);
}
