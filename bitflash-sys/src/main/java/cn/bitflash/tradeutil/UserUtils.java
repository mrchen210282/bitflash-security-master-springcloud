package cn.bitflash.tradeutil;


import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserGTCidEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value="bitflash-login",fallbackFactory =UserLoginFallback.class)
public interface UserUtils {

    @PostMapping("/api/login/inner/selectGT")
    UserGTCidEntity selectGT(@RequestBody Map<String,Object> map);

    @PostMapping("/api/login/inner/selectToken")
    TokenEntity selectToken(@RequestBody Map<String,Object> map);


}
