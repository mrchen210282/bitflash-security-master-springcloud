package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.UserLoginFeign;
import cn.bitflash.login.UserGTCidEntity;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserLoginFallback implements FallbackFactory<UserLoginFeign> {

    private static final Logger log=LoggerFactory.getLogger(UserLoginFallback.class);
    @Override
    public UserLoginFeign create(Throwable throwable) {
        return u->{
            log.error("查询个推cid出错原因："+throwable.getMessage());
            return new UserGTCidEntity();
        };
    }
}
