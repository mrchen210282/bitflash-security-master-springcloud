package cn.bitflash.feignController;

import cn.bitflash.annotation.Login;
import cn.bitflash.user.TokenEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "bitflash-login")
public interface LoginFeign {

    @PostMapping("/api/user-app/logout")
    public Map<String,Object> logout(@RequestBody TokenEntity tokenEntity);

    @PostMapping("/api/token")
    TokenEntity getTokenByToken(@RequestParam("token")String token);

}
