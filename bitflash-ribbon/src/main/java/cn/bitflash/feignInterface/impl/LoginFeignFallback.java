package cn.bitflash.feignInterface.impl;


import cn.bitflash.feignInterface.BitflashLoginFeign;
import cn.bitflash.login.LoginForm;
import cn.bitflash.login.RegisterForm;
import cn.bitflash.utils.R;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginFeignFallback implements FallbackFactory<BitflashLoginFeign> {

    private static final Logger log=LoggerFactory.getLogger(UserFeignFallback.class);

    @Override
    public BitflashLoginFeign create(Throwable throwable) {
        return new BitflashLoginFeign() {
            @Override
            public R register(RegisterForm form) {
                log.error("手机注册失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R register2(String mobile, String pwd, String invitationCode, HttpServletResponse response) {
                log.error("网页注册失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R sent(String mobile, String type, HttpServletResponse response) {
                log.error("sent接口失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R authorityValidate(HttpServletRequest request) {
                log.error("authorityValidate接口失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R login(LoginForm form) {
                log.error("登录失败：-----"+throwable.getMessage());
                return R.error();
            }

            @Override
            public R logout() {
                log.error("登出失败：-----"+throwable.getMessage());
                return R.error();
            }
        };
    }
}
