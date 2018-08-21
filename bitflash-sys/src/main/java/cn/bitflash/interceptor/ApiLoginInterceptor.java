package cn.bitflash.interceptor;

import cn.bitflash.annotation.Login;
import cn.bitflash.exception.RRException;
import cn.bitflash.feignInterface.UserLoginFeign;
import cn.bitflash.login.TokenEntity;
import cn.bitflash.utils.RedisUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static cn.bitflash.utils.Common.MOBILE;
import static cn.bitflash.utils.Common.TOKEN;

@Component
public class ApiLoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisUtils redisUtils;


    public static final String UID = "uid";

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
        String token = (String) request.getSession().getAttribute(TOKEN);

        //token为空
        if (StringUtils.isBlank(token)) {
            throw new RRException("参数不能为空");
        }
        TokenEntity tokenEntity = redisUtils.get(token,TokenEntity.class);

        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(UID, tokenEntity.getUid());
        return true;

    }
}
