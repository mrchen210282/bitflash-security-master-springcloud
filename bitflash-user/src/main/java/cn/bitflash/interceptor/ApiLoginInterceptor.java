package cn.bitflash.interceptor;

import cn.bitflash.annotation.Login;
import cn.bitflash.exception.RRException;
import cn.bitflash.feign.LoginFeign;
import cn.bitflash.login.TokenEntity;
import cn.bitflash.utils.AESTokenUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApiLoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private LoginFeign loginFeign;

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

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("token",realToken);
        TokenEntity tokenEntity=loginFeign.selectOne(new EntityWrapper<TokenEntity>().eq("token",realToken));
        if(tokenEntity==null){
            throw new RRException("token信息错误" );
        }
        if(!tokenEntity.getUid().equals(uid)){
            throw new RRException("token信息与用户信息不符");
        }
        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(UID, tokenEntity.getUid());
        return true;

    }
}
