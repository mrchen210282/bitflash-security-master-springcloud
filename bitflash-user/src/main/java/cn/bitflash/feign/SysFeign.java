package cn.bitflash.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "bitflash-sys")
public interface SysFeign {

    @PostMapping("/api/sys/withinVal/getval")
    String getVal(@RequestParam("key") String key);

}
