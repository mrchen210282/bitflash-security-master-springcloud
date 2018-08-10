package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.BitflashSysFeign;
import cn.bitflash.utils.R;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysFeignFallback implements FallbackFactory<BitflashSysFeign> {

    private static final Logger log=LoggerFactory.getLogger(UserFeignFallback.class);

    @Override
    public BitflashSysFeign create(Throwable throwable) {
        return new BitflashSysFeign() {
            @Override
            public R sendSingleMsg(String uid, String id) {
                log.error("发送个推消息失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R update(String appid, String version, String imei) {
                log.error("获取app版本失败：------"+throwable.getMessage());
                return R.error();
            }
        };
    }
}
