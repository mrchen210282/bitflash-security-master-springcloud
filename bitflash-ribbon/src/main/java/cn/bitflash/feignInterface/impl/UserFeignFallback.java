package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.BitflashUserFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserFeignFallback implements FallbackFactory<BitflashUserFeign> {
    @Override
    public BitflashUserFeign create(Throwable throwable) {
        return new BitflashUserFeign() {

        };
    }
}
