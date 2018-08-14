package cn.bitflash.controller;


import cn.bitflash.service.PlatFormConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sys/inner")
public class ApiSysInnerController {

    @Autowired
    private PlatFormConfigService platFormConfigService;

    @PostMapping("/getval")
    public String getVal(@RequestParam("key")String key){
        return platFormConfigService.getVal(key);
    }
}
