package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.LoginFeign;
import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserEntity;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LoginFallback implements FallbackFactory<LoginFeign> {

    private static final Logger log= LoggerFactory.getLogger(LoginFallback.class);

    @Override
    public LoginFeign create(Throwable throwable) {
        return new LoginFeign() {
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
        };
    }
}
