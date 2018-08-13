package cn.bitflash.feign;

import cn.bitflash.feign.impl.SysFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "bitflash-sys",fallbackFactory = SysFallback.class)
public interface SysFeign {

    @PostMapping("/api/sys/inner/getval")
    String getVal(@RequestParam("key") String key);

}
