package cn.bitflash.DefaultFallBack;

import cn.bitflash.feign.LoginRibbon;
import cn.bitflash.login.LoginForm;
import cn.bitflash.utils.R;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultFallBackFactory implements FallbackFactory<LoginRibbon> {
    private static final Logger log=LoggerFactory.getLogger(DefaultFallBackFactory.class);

    @Override
    public LoginRibbon create(Throwable throwable) {
        return new LoginRibbon(){


            @Override
            public R login(LoginForm form) {
                log.error("调用登录接口错误原因："+throwable);
                return R.error();
            }

            @Override
            public R logout(String uid) {
                log.error("调用登出接口错误原因："+throwable);
                return R.error();
            }
        };
    }
}
