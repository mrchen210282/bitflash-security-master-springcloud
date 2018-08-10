package cn.bitflash.feignInterface;


import cn.bitflash.feignInterface.impl.UserLoginFallback;
import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserGTCidEntity;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value="bitflash-login",fallbackFactory =UserLoginFallback.class)
public interface UserLoginFeign {

    @PostMapping("/api/login/wthinGT/selectOne")
    UserGTCidEntity selectOneByGT(@RequestBody Wrapper<UserGTCidEntity> wrapper);

    @PostMapping("/api/login/withinToken/selectOne")
    TokenEntity selectOneByToken(@RequestBody Wrapper<TokenEntity> entityWrapper);


}
