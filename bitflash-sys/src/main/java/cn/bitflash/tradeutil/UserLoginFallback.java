package cn.bitflash.tradeutil;

import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserGTCidEntity;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class UserLoginFallback implements FallbackFactory<UserUtils> {

    private static final Logger log=LoggerFactory.getLogger(UserLoginFallback.class);
    @Override
    public UserUtils create(Throwable throwable) {
        return new UserUtils() {
            @Override
            public UserGTCidEntity selectGT(Map<String, Object> map) {
                log.error("查询个推cid出错原因："+throwable);
                return new UserGTCidEntity();
            }

            @Override
            public TokenEntity selectToken(Map<String, Object> map) {
                log.error("查询token出错原因："+throwable);
                return new TokenEntity();
            }


        };
    }
}
