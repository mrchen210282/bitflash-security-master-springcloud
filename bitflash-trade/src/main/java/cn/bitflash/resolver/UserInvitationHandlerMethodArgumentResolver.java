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

package cn.bitflash.resolver;

import cn.bitflash.annotation.UserInvitationCode;
import cn.bitflash.feign.UserFeign;
import cn.bitflash.interceptor.ApiLoginInterceptor;
import cn.bitflash.user.UserInvitationCodeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 有@UserInvitationCode注解的方法参数，注入当前登录用户
 *
 * @author eric
 */
@Component
public class UserInvitationHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private UserFeign userFeign;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserInvitationCodeEntity.class)
                && parameter.hasParameterAnnotation(UserInvitationCode.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request,
                                  WebDataBinderFactory factory) throws Exception {
        // 获取用户ID
        Object object = request.getAttribute(ApiLoginInterceptor.UID, RequestAttributes.SCOPE_REQUEST);
        if (object == null) {
            return null;
        }

        // 获取用户信息
        UserInvitationCodeEntity userInvitationCodeEntity = userFeign.selectRelation(object.toString());

        return userInvitationCodeEntity;
    }
}
