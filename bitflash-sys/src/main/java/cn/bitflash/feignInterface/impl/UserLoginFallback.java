package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.UserLoginFeign;
import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserGTCidEntity;
import com.baomidou.mybatisplus.mapper.Wrapper;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class UserLoginFallback implements FallbackFactory<UserLoginFeign> {

    private static final Logger log=LoggerFactory.getLogger(UserLoginFallback.class);
    @Override
    public UserLoginFeign create(Throwable throwable) {
        return new UserLoginFeign() {
            @Override
            public UserGTCidEntity selectOneByGT(Map<String, Object> map) {
                log.error("查询个推cid出错原因："+throwable);
                return new UserGTCidEntity();
            }

            @Override
            public TokenEntity selectOneByToken(Map<String, Object> map) {
                log.error("查询token出错原因："+throwable);
                return new TokenEntity();
            }


        };
    }
}
