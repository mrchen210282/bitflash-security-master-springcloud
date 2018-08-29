package cn.bitflash.loginutil;

import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "bitflash-login",fallbackFactory = loginFallback.class)
public interface LoginUtils {

    @PostMapping("/api/login/inner/selectToken")
    TokenEntity selectOne(@RequestBody Map<String,Object> map);

    @PostMapping("/api/login/inner/selectUser")
    UserEntity selectOneByUser(@RequestBody Map<String,Object> map);

    @PostMapping("/api/login/inner/updateByUser")
    boolean update(@RequestBody UserEntity userEntity);

}
