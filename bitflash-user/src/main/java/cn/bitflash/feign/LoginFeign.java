package cn.bitflash.feign;

import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserEntity;
import cn.bitflash.trade.UserAccountEntity;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "bitflash-login")
public interface LoginFeign {

    @PostMapping("/api/login/withinToken/selectOne")
    public TokenEntity selectOne(@RequestBody Wrapper<TokenEntity> entityWrapper);

    @PostMapping("/api/login/withinUser/selectOne")
    public UserEntity selectOneByUser(@RequestBody Wrapper<UserEntity> entityWrapper);

    @PostMapping("/api/login/withinUser/update")
    public boolean update(@RequestBody UserEntity userEntity,@RequestParam("entityWrapper") Wrapper<UserEntity> entityWrapper);

}
