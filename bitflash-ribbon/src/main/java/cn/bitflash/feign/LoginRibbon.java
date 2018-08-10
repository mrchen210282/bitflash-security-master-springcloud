package cn.bitflash.feign;

import cn.bitflash.DefaultFallBack.DefaultFallBackFactory;
import cn.bitflash.login.LoginForm;
import cn.bitflash.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * name为资源提供者的spring.application.name
 * @author eric
 */
@FeignClient(value="bitflash-login",fallbackFactory = DefaultFallBackFactory.class)
public interface LoginRibbon {
    /**
     *
     * @return
     */
    @PostMapping("/api/user-app/login")
    public R login(@RequestBody LoginForm form);

    @PostMapping("/api/user-app/logout")
    public R logout(String uid);
}
