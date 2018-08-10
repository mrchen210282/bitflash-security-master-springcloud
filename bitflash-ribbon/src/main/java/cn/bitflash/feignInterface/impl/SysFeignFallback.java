package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.BitflashSysFeign;
import feign.hystrix.FallbackFactory;

public class SysFeignFallback implements FallbackFactory<BitflashSysFeign> {
    @Override
    public BitflashSysFeign create(Throwable throwable) {
        return new BitflashSysFeign() {
        };
    }
}
