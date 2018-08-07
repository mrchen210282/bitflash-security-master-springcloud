package cn.bitflash.config;

import cn.bitflash.interceptor.ApiLoginInterceptor;
import cn.bitflash.resolver.DecodeTokenHandlerMethodArgumentResolver;
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
    private DecodeTokenHandlerMethodArgumentResolver decodeTokenHandlerMethodArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiLoginInterceptor()).addPathPatterns("/api/**" );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(decodeTokenHandlerMethodArgumentResolver);

    }
}