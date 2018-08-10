package cn.bitflash.controller.login;

import cn.bitflash.feignInterface.BitflashLoginFeign;
import cn.bitflash.login.RegisterForm;
import cn.bitflash.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/reg")
public class ApiRegisterController {

    @Autowired
    private BitflashLoginFeign bitflashLoginFeign;

    @PostMapping("register")
    public R register(@RequestBody RegisterForm form, HttpServletResponse response) {
        bitflashLoginFeign.register(form);
        return R.ok();
    }

    @GetMapping("register2")
    public R register2(@RequestParam String mobile, @RequestParam String pwd,
                       @RequestParam(value = "invitationCode", required = false) String invitationCode,
                       HttpServletResponse response) {
        return bitflashLoginFeign.register2(mobile,pwd,invitationCode,response);
    }

    @RequestMapping("getVerifyCode")
    public R sent(@RequestParam String mobile, @RequestParam String type, HttpServletResponse response){
        return bitflashLoginFeign.sent(mobile,type,response);
    }

    @GetMapping("authorityValidate")
    public R authorityValidate(HttpServletRequest request){
        return bitflashLoginFeign.authorityValidate(request);
    }

}
