package cn.bitflash.feignInterface;

import cn.bitflash.feignInterface.impl.SysFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Lazy(true)
@FeignClient(value = "bitflash-sys",fallbackFactory = SysFallback.class)
public interface SysFeign {

    @PostMapping("/api/sys/inner/getval")
    String getVal(@RequestParam("key") String key);
}
