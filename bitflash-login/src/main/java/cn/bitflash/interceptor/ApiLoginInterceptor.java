package cn.bitflash.interceptor;

import cn.bitflash.annotation.Login;
import cn.bitflash.exception.RRException;
import cn.bitflash.login.TokenEntity;
import cn.bitflash.redisConfig.RedisKey;
import cn.bitflash.service.TokenService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ApiLoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private TokenService tokenService;

    public static final String UID="uid";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Login annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        } else {
            return true;
        }

        if (annotation == null) {
            return true;
        }
        String mobile = (String) request.getSession().getAttribute(RedisKey.MOBILE.toString());
        String token = (String) request.getSession().getAttribute(RedisKey.TOKEN.toString());
        if (StringUtils.isBlank(mobile)) {
            mobile = (String) request.getAttribute(RedisKey.MOBILE.toString());
        }
        if (StringUtils.isBlank(token)) {
            token = (String) request.getAttribute(RedisKey.TOKEN.toString());
        }
        //token为空
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(token)) {
            throw new RRException("参数不能为空");
        }
        TokenEntity tokenEntity = tokenService.selectOne(new EntityWrapper<TokenEntity>().eq("token", token));
        if (tokenEntity == null) {
            throw new RRException("token信息错误");
        }
        String userMobile = tokenEntity.getMobile();
        if (!userMobile.equals(mobile)) {
            throw new RRException("token信息与用户信息不符");
        }
        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(UID, tokenEntity.getUid());
        return true;

    }
}
