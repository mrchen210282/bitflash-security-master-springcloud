package cn.bitflash.feign.impl;

import cn.bitflash.feign.PlatFormConfigFeign;
import cn.bitflash.feign.UserFeign;
import cn.bitflash.login.UserEntity;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserRelationEntity;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Component
public class PlatFormConfigFeignback  implements FallbackFactory<PlatFormConfigFeign> {

    private static final Logger log= LoggerFactory.getLogger(PlatFormConfigFeignback.class);

    @Override
    public PlatFormConfigFeign create(Throwable throwable) {
        return new PlatFormConfigFeign() {
            @Override
            public String getVal(@RequestParam("uid") String uid) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return null;
            }
        };
    }
}
