package cn.bitflash.feign;

import cn.bitflash.feign.impl.loginFallback;
import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserEntity;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "bitflash-login",fallbackFactory = loginFallback.class)
public interface LoginFeign {

    @PostMapping("/api/login/inner/selectToken")
    TokenEntity selectOne(@RequestBody Map<String,Object> map);

    @PostMapping("/api/login/inner/selectUser")
    UserEntity selectOneByUser(@RequestBody Map<String,Object> map);

    @PostMapping("/api/login/inner/updateByUser")
    boolean update(@RequestBody UserEntity userEntity);

}
