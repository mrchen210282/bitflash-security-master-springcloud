package cn.bitflash.resolver;

import cn.bitflash.annotation.DecodeToken;
import cn.bitflash.exception.RRException;
import cn.bitflash.feignInterface.LoginFeign;
import cn.bitflash.redisConfig.RedisKey;
import cn.bitflash.user.TokenEntity;
import cn.bitflash.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class DecodeTokenHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private LoginFeign loginFeign;

    @Autowired
    private RedisUtils redisUtils;

    private final String MOBILE = "mobile";
    private final String UID = "uid";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(TokenEntity.class)
                && parameter.hasParameterAnnotation(DecodeToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request,
                                  WebDataBinderFactory factory) throws Exception {
        String mobile = request.getHeader(MOBILE);
        if (mobile.length() == 0 || mobile == null) {
            mobile = request.getParameter(MOBILE);
        }
        String secretUid = request.getParameter(UID);
        if (secretUid.length() == 0 || secretUid == null) {
            secretUid = request.getParameter(UID);
        }
        if (mobile == null || secretUid == null) {
            throw new RRException("参数不能为空");
        }
        String token = redisUtils.get(RedisKey.LOGIN_ + mobile);

        TokenEntity tokenEntity = loginFeign.getTokenByToken(token);
        return null;
    }


}
