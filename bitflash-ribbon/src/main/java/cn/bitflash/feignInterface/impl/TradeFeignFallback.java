package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.BitflashTradeFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class TradeFeignFallback implements FallbackFactory<BitflashTradeFeign> {
    @Override
    public BitflashTradeFeign create(Throwable throwable) {
        return new BitflashTradeFeign() {

        };
    }
}
