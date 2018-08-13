package cn.bitflash.feign;

import cn.bitflash.feign.impl.PlatFormConfigFeignback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "bitflash-sys",fallbackFactory = PlatFormConfigFeignback.class)
public interface PlatFormConfigFeign {

    @PostMapping("/api/sys/getVal")
    String getVal(@RequestParam("uid") String uid);
}
