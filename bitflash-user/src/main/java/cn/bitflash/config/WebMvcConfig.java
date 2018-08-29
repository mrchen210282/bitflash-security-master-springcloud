package cn.bitflash.config;

import cn.bitflash.interceptor.ApiLoginInterceptor;
import cn.bitflash.resolver.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * MVC配置
 *
 * @author eric
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    ApiLoginInterceptor apiLoginInterceptor;


    @Autowired
    PayPasswordHandlerMethodArgumentResolver payPasswordHandlerMethodArgumentResolver;

    @Autowired
    UserInvitationHandlerMethodArgumentResolver userInvitationHandlerMethodArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiLoginInterceptor).addPathPatterns("/api/**" );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(payPasswordHandlerMethodArgumentResolver);
        argumentResolvers.add(userInvitationHandlerMethodArgumentResolver);

    }
}