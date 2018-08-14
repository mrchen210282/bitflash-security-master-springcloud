package cn.bitflash.feignInterface;

import cn.bitflash.feignInterface.impl.SysFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "bitflash-sys",fallbackFactory = SysFallback.class)
public interface SysFeign {

    @PostMapping("/api/sys/inner/getVal")
    String getVal(@RequestParam("uid") String uid);
}
