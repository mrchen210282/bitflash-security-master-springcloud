package cn.bitflash.interceptor;

import cn.bitflash.annotation.Login;
import cn.bitflash.exception.RRException;
import cn.bitflash.feign.LoginFeign;
import cn.bitflash.login.TokenEntity;
import cn.bitflash.redisConfig.RedisKey;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ApiLoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private LoginFeign loginFeign;

    public static final String UID="uid";
    public static final String MOBILE="mobile";

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
            mobile = request.getParameter(MOBILE);
        }
        //token为空
        if (StringUtils.isBlank(mobile)) {
            throw new RRException("参数不能为空");
        }
        if(token==null){
            throw new RRException("访问地址失败，请正确访问" );
        }
        TokenEntity tokenEntity=loginFeign.selectOne(new ModelMap("token",token));
        String userMobile=tokenEntity.getMobile();
        if(tokenEntity==null){
            throw new RRException("token信息错误" );
        }
        if(!userMobile.equals(mobile)){
            throw new RRException("token信息与用户信息不符");
        }
        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(UID, tokenEntity.getUid());
        return true;

    }
}
