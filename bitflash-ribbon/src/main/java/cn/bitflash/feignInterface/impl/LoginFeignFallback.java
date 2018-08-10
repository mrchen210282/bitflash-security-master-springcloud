package cn.bitflash.feignInterface.impl;


import cn.bitflash.feignInterface.BitflashLoginFeign;
import cn.bitflash.login.RegisterForm;
import cn.bitflash.utils.R;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginFeignFallback implements FallbackFactory<BitflashLoginFeign> {
    @Override
    public BitflashLoginFeign create(Throwable throwable) {
        return new BitflashLoginFeign() {
            @Override
            public R register(RegisterForm form) {
                return null;
            }

            @Override
            public R register2(String mobile, String pwd, String invitationCode, HttpServletResponse response) {
                return null;
            }

            @Override
            public R sent(String mobile, String type, HttpServletResponse response) {
                return null;
            }

            @Override
            public R authorityValidate(HttpServletRequest request) {
                return null;
            }
        };
    }
}
