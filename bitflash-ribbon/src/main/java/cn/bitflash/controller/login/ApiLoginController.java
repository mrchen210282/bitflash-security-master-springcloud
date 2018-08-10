package cn.bitflash.controller.login;

import cn.bitflash.feignInterface.BitflashLoginFeign;
import cn.bitflash.login.LoginForm;
import cn.bitflash.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class ApiLoginController {

    @Autowired
    private BitflashLoginFeign bitflashLoginFeign;

    @PostMapping("login")
    public R login(@RequestBody LoginForm form) {
        return bitflashLoginFeign.login(form);
    }

    @PostMapping("logout")
    public R logout() {
        return bitflashLoginFeign.logout();
    }
}
