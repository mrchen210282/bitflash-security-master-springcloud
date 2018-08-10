package cn.bitflash.controller.sys;

import cn.bitflash.feignInterface.BitflashSysFeign;
import cn.bitflash.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api" )
public class ApiSystemController {

    @Autowired
    private BitflashSysFeign bitflashSysFeign;

    @GetMapping("update")
    public R update(@RequestParam String appid, @RequestParam String version, @RequestParam String imei){
        return bitflashSysFeign.update(appid,version,imei);
    }
}
