package cn.bitflash.sysutils;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SysFallback implements FallbackFactory<SysUtils> {

    private static final Logger log=LoggerFactory.getLogger(SysFallback.class);

    @Override
    public SysUtils create(Throwable throwable) {
        return new SysUtils() {
            @Override
            public String getVal(String params) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return null;
            }
        };
    }
}
