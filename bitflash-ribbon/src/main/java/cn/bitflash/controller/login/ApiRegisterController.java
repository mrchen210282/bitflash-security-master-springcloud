package cn.bitflash.controller.login;

import cn.bitflash.login.RegisterForm;
import cn.bitflash.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/reg")
public class ApiRegisterController {



    @PostMapping("register")
    public R register(@RequestBody RegisterForm form, HttpServletResponse response) {

    }
}
