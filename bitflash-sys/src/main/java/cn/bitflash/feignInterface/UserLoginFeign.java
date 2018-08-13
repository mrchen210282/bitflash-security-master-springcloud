package cn.bitflash.feignInterface;


import cn.bitflash.feignInterface.impl.UserLoginFallback;
import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserGTCidEntity;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value="bitflash-login",fallbackFactory =UserLoginFallback.class)
public interface UserLoginFeign {

    @PostMapping("/api/login/wthinGT/selectOne")
    UserGTCidEntity selectOneByGT(@RequestBody Map<String,Object> map);

    @PostMapping("/api/login/withinToken/selectOne")
    TokenEntity selectOneByToken(@RequestBody Map<String,Object> map);


}
