/*
package cn.bitflash.DefaultFallBack;

import cn.bitflash.feign.LoginRibbon;
import cn.bitflash.user.LoginForm;
import cn.bitflash.utils.R;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserDefaultFallBackFactory implements FallbackFactory<LoginRibbon> {
    private static final Logger log=LoggerFactory.getLogger(UserDefaultFallBackFactory.class);

    @Override
    public LoginRibbon create(Throwable throwable) {
        return new LoginRibbon(){


            @Override
            public R login(LoginForm form) {
                return null;
            }

            @Override
            public R logout(String uid) {
                log.error("调用登出接口错误原因："+throwable);
                return R.error();
            }
        };
    }
}
*/
