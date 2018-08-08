package cn.bitflash.interceptor;

import cn.bitflash.annotation.Login;
import cn.bitflash.exception.RRException;
import cn.bitflash.service.TokenService;
import cn.bitflash.user.TokenEntity;
import cn.bitflash.utils.AESTokenUtil;
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

    public static final String TIME="time";
    public static final String TOKEN="token";
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
        System.out.println("----------------------------------------------------------------------");
        String secretTime = request.getHeader(TIME );
        String secretToken = request.getHeader(TOKEN);
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(secretTime)) {
            secretTime = request.getParameter(TIME );
        }
        if (StringUtils.isBlank(secretToken)) {
            secretToken = request.getParameter(TOKEN );
        }
        //token为空
        if (StringUtils.isBlank(secretTime) || StringUtils.isBlank(secretToken)) {
            throw new RRException("参数不能为空");

        }
        String uid=request.getParameter(UID);
        String realToken=AESTokenUtil.getToken(secretTime,secretToken);
        uid=AESTokenUtil.getData(realToken,uid);
        TokenEntity tokenEntity=tokenService.selectOne(new EntityWrapper<TokenEntity>().eq("token",realToken));
        if(tokenEntity==null){
            throw new RRException("token信息错误" );
        }
        if(!tokenEntity.getUid().equals(uid)){
            throw new RRException("token信息与用户信息不符");
        }
        return true;

    }
}
