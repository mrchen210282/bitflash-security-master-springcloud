/**
 * Copyright 2018 贝莱科技 http://www.bitflash.cn
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package cn.bitflash.interceptor;


import cn.bitflash.annotation.Login;
import cn.bitflash.exception.RRException;
import cn.bitflash.feign.LoginFeign;
import cn.bitflash.login.TokenEntity;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.utils.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限(Token)验证
 *
 * @author eric
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private LoginFeign tokenService;

    public static final String USER_KEY = "userId";

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

        //从header中获取token
        String token = request.getHeader("token" );
        String mobile = request.getHeader("mobile" );
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token" );
        }
        if (StringUtils.isBlank(mobile)) {
            mobile = request.getParameter("mobile" );
        }

        //token为空
        if (StringUtils.isBlank(token) || StringUtils.isBlank(mobile)) {
            throw new RRException("参数不能为空" );

        }

        //查询token信息
        TokenEntity tokenEntity = tokenService.selectOne(new EntityWrapper<TokenEntity>().eq("mobile",mobile));
        if (tokenEntity == null) {
            throw new RRException("请重新登录获取token" );
        }
        String tokenDb = MD5Util.stringToMD5(tokenEntity.getToken() + mobile.substring(2, 3) + mobile.substring(5, 6) + mobile.substring(8, 10));
        //System.out.println(token + "-------" + tokenDb);
        if (!tokenDb.equals(token)) {
            throw new RRException("token值不正确" );
        }


        if (tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
            throw new RRException("登录过期，请重新登录" );
        }


        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(USER_KEY, tokenEntity.getUid());

        return true;
    }
}
