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
    private ApiLoginInterceptor apiLoginInterceptor;

    @Autowired
    private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;

    @Autowired
    private PayPasswordHandlerMethodArgumentResolver payPasswordHandlerMethodArgumentResolver;

    @Autowired
    private UserAccountHandlerMethodArgumentResolver userAccountHandlerMethodArgumentResolver;

    @Autowired
    private UserInvitationHandlerMethodArgumentResolver userInvitationHandlerMethodArgumentResolver;

    @Autowired
    private UserRelationHandlerMethodArgumentResolver userRelationHandlerMethodArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiLoginInterceptor).addPathPatterns("/api/**" );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
        argumentResolvers.add(payPasswordHandlerMethodArgumentResolver);
        argumentResolvers.add(userAccountHandlerMethodArgumentResolver);
        argumentResolvers.add(userInvitationHandlerMethodArgumentResolver);
        argumentResolvers.add(userRelationHandlerMethodArgumentResolver);
    }
}