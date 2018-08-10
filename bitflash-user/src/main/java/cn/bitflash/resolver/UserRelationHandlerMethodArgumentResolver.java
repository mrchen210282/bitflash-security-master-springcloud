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

import cn.bitflash.annotation.UserRelation;
import cn.bitflash.interceptor.ApiLoginInterceptor;
import cn.bitflash.service.UserRelationService;
import cn.bitflash.user.UserRelationJoinAccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

/**
 * 有@UserRelation注解的方法参数，注入当前登录用户
 *
 * @author eric
 */
@Component
public class UserRelationHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private UserRelationService userRelationService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(List.class)
                && parameter.hasParameterAnnotation(UserRelation.class);
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
        List<UserRelationJoinAccountEntity> ura = userRelationService.selectTreeNodes((String) object);
        return ura;
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (ura != null && ura.size() > 1) {
//			map.put("lft_a", ura.get(0).getLftAchievement());
//			map.put("rgt_a", ura.get(0).getRgtAchievement());
//			map.put("c_lft_realname", "未排点");
//			map.put("c_rgt_realname", "未排点");
//			if (ura.size() > 1) {
//				map.put("c_lft_realname", ura.get(1).getRealname());
//				int cl_rgt = ura.get(1).getRgt();
//				for (UserRelationJoinAccountEntity t : ura) {
//					if (t.getLft() == (cl_rgt + 1)) {
//						map.put("c_rgt_realname", t.getRealname());
//					}
//
//				}
//			}
//		}
//
//		return map;
    }
}
