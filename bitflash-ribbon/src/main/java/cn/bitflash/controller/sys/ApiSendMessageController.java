package cn.bitflash.controller.sys;

import cn.bitflash.feignInterface.BitflashSysFeign;
import cn.bitflash.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/msg")
public class ApiSendMessageController {

    @Autowired
    private BitflashSysFeign bitflashSysFeign;

    @PostMapping("singleMsg")
    public R sendSingleMsg(@RequestParam String uid, @RequestParam String id){
        return bitflashSysFeign.sendSingleMsg(uid,id);
    }

}
