package cn.bitflash.feignInterface;

import cn.bitflash.feignInterface.impl.SysFeignFallback;
import cn.bitflash.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "bitflash-sys",fallbackFactory =SysFeignFallback.class)
public interface BitflashSysFeign {

    @PostMapping("/api/msg/singleMsg")
    R sendSingleMsg(@RequestParam("uid") String uid, @RequestParam("id") String id);

    @GetMapping("update" )
    public R update(@RequestParam("appid") String appid, @RequestParam("version") String version, @RequestParam("imei") String imei);
}
