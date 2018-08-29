package cn.bitflash.sysutils;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "bitflash-sys",fallbackFactory = SysFallback.class)
public interface SysUtils {

    @PostMapping("/api/sys/inner/getval")
    String getVal(@RequestParam("key") String key);

}
