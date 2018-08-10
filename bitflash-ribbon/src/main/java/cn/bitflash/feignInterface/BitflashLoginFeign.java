package cn.bitflash.feignInterface;


import cn.bitflash.feignInterface.impl.LoginFeignFallback;
import cn.bitflash.login.LoginForm;
import cn.bitflash.login.RegisterForm;
import cn.bitflash.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * biflash-login对应feign接口
 * @author eric
 */
@FeignClient(value = "bitflash-login",fallbackFactory = LoginFeignFallback.class)
public interface BitflashLoginFeign {

    /**
     * ApiRegisterController
     */

    /**
     * 注册
     * @param form
     * @return
     */
    @PostMapping("/api/reg/register")
    R register(@RequestBody RegisterForm form);

    /**
     * 网页注册
     * @param mobile
     * @param pwd
     * @param invitationCode
     * @param response
     * @return
     */
    @PostMapping("/api/reg/register2")
    R register2(@RequestParam("mobile") String mobile, @RequestParam("pwd") String pwd,
                @RequestParam(value = "invitationCode", required = false) String invitationCode,
                HttpServletResponse response);

    @RequestMapping("/api/reg/getVerifyCode")
    R sent(@RequestParam("mobile") String mobile, @RequestParam("type") String type, HttpServletResponse response);

    @GetMapping("/api/reg/authorityValidate")
    R authorityValidate(HttpServletRequest request);

    /**
     * LoginController
     */

    /**
     * 登录
     * @param form
     * @return
     */
    @PostMapping("/api/login/login")
    R login(@RequestBody LoginForm form);

    /**
     * 登出
     * @return
     */
    @PostMapping("/api/login/logout" )
    R logout();
}
