package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.SysFeign;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class SysFallback implements FallbackFactory<SysFeign> {

    private static final Logger log = LoggerFactory.getLogger(SysFallback.class);

    @Override
    public SysFeign create(Throwable throwable) {
        return u -> {
            log.error("获取地址失败-----,错误原因:" + throwable);
            return null;
        };
    }
}
