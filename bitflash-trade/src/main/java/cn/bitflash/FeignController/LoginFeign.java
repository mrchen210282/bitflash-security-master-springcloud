package cn.bitflash.FeignController;

import cn.bitflash.login.TokenEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "bitflash-login")
public interface LoginFeign {

    @PostMapping("/api/user-app/logout")
    public Map<String,Object> logout(@RequestBody TokenEntity tokenEntity);

    @PostMapping("/api/token")
    TokenEntity getTokenByToken(@RequestBody Map<String,Object> map);

}
