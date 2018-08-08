package cn.bitflash.controller;

import cn.bitflash.feignController.LoginFeign;
import cn.bitflash.user.TokenEntity;
import cn.bitflash.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chen
 */
@RestController
@RequestMapping("/api/external")
public class ApiExternalController {

    @Autowired
    private LoginFeign loginFeign;

    @PostMapping("test")
    public R testFeign(@RequestBody TokenEntity tokenEntity){
        System.out.println(tokenEntity.getUid());
        Map<String,Object> map=loginFeign.logout(tokenEntity);
        return R.ok(map);
    }

}
