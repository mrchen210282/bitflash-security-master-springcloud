package cn.bitflash.loginutil;

import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserEntity;
import cn.bitflash.login.UserGTCidEntity;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LoginFallback implements FallbackFactory<LoginUtils> {

    private static final Logger log= LoggerFactory.getLogger(LoginFallback.class);

    @Override
    public LoginUtils create(Throwable throwable) {
        return new LoginUtils() {
            @Override
            public List<UserEntity> selectUserOne(Map<String, Object> params) {
                log.error("获取用户信息失败-----:"+throwable);
                return new ArrayList<>();
            }

            @Override
            public TokenEntity selectOne(Map<String, Object> map) {
                log.error("获取用户信息失败-----:"+throwable);
                return new TokenEntity();
            }

            @Override
            public UserEntity selectOneByUser(Map<String, Object> map) {
                log.error("获取用户信息失败-----:"+throwable);
                return new UserEntity();
            }

            @Override
            public UserGTCidEntity selectGT(@RequestBody Map<String,Object> map){
                log.error("获取用户信息失败-----:"+throwable);
                return new UserGTCidEntity();
            }
        };
    }
}
