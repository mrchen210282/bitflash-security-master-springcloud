package cn.bitflash.feign.impl;

import cn.bitflash.feign.LoginFeign;
import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserEntity;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class loginFallback implements FallbackFactory<LoginFeign> {

    private static final Logger log=LoggerFactory.getLogger(loginFallback.class);


    @Override
    public LoginFeign create(Throwable throwable) {
        return new LoginFeign() {
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
