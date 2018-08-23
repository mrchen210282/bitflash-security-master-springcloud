package cn.bitflash.resolver;

import cn.bitflash.annotation.PayPassword;
import cn.bitflash.feignInterface.UserFeign;
import cn.bitflash.interceptor.ApiLoginInterceptor;
import cn.bitflash.user.UserPayPwdEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class PayPasswordHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserFeign userFeign;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserPayPwdEntity.class) && parameter.hasParameterAnnotation(PayPassword.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest request, WebDataBinderFactory webDataBinderFactory) throws Exception {
        // 获取用户ID
        Object object = request.getAttribute(ApiLoginInterceptor.UID, RequestAttributes.SCOPE_REQUEST);
        UserPayPwdEntity payPwd = userFeign.selectUserPayPwd(new ModelMap("uid", object.toString()));
        return payPwd == null ? null : payPwd;
    }

}
