package cn.bitflash.feign;

import cn.bitflash.feign.impl.LoginFallback;
import cn.bitflash.feign.impl.SysFallback;
import cn.bitflash.login.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "bitflash-login",fallbackFactory = LoginFallback.class)
public interface LoginFeign {

    @PostMapping("/api/user/inner/selectUserOne")
    public List<UserEntity> selectUserOne(@RequestParam("params") Map<String, Object> params);
}
