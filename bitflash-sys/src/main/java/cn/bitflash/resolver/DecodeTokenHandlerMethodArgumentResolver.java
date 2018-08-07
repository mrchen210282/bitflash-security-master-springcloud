package cn.bitflash.resolver;

import cn.bitflash.annotation.DecodeToken;
import cn.bitflash.interceptor.ApiLoginInterceptor;
import cn.bitflash.user.TokenEntity;
import cn.bitflash.utils.AESTokenUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class DecodeTokenHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(TokenEntity.class)
                && parameter.hasParameterAnnotation(DecodeToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request,
                                  WebDataBinderFactory factory) throws Exception {

        String uid = request.getParameter(ApiLoginInterceptor.UID);
        String secretTime = request.getHeader(ApiLoginInterceptor.TIME);
        String secretToken = request.getHeader(ApiLoginInterceptor.TOKEN);
        String realToken=AESTokenUtil.getToken(secretTime,secretToken);
        String realUid=AESTokenUtil.getData(realToken,uid);
        TokenEntity token=new TokenEntity();
        token.setToken(realToken);
        token.setUid(realUid);
        return token;
    }


}
