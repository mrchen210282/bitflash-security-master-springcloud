package cn.bitflash.loginutil;

import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserEntity;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class loginFallback implements FallbackFactory<LoginUtils> {

    private static final Logger log=LoggerFactory.getLogger(loginFallback.class);


    @Override
    public LoginUtils create(Throwable throwable) {
        return new LoginUtils() {
            @Override
            public TokenEntity selectOne(Map<String,Object> map) {
                log.error("查询账户失败-----:"+throwable.getMessage());
                return new TokenEntity();
            }

            @Override
            public UserEntity selectOneByUser(Map<String,Object> map) {
                log.error("查询用户失败-----:"+throwable.getMessage());
                return new UserEntity();
            }

            @Override
            public boolean update(UserEntity userEntity) {
                log.error("更新用户失败-----:"+throwable.getMessage());
                return false;
            }
        };
    }
}
