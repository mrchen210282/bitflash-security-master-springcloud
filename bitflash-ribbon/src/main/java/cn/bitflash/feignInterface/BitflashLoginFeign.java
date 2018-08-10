package cn.bitflash.feignInterface;


import cn.bitflash.feignInterface.impl.LoginFeignFallback;
import cn.bitflash.login.RegisterForm;
import cn.bitflash.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
@FeignClient(value = "bitflash-login",fallbackFactory = LoginFeignFallback.class)
public interface BitflashLoginFeign {

    @PostMapping("/api/reg/register")
    R register(@RequestBody RegisterForm form);

    @PostMapping("/api/reg/register2")
    R register2(@RequestParam("mobile") String mobile, @RequestParam("pwd") String pwd,
                @RequestParam(value = "invitationCode", required = false) String invitationCode,
                HttpServletResponse response);

    @RequestMapping("/api/reg/getVerifyCode")
    R sent(@RequestParam("mobile") String mobile, @RequestParam("type") String type, HttpServletResponse response);

    @GetMapping("authorityValidate")
    R authorityValidate(HttpServletRequest request);
}
