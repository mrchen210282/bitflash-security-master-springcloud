package cn.bitflash.config;

import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DisableHystrixConfiguration {
    /**
     * 当需要禁用Hystrix时，需要在属性中
     * @return
     */
    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder(){
        return Feign.builder();
    }
}
