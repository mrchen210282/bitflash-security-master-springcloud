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

package cn.bitflash.service.impl;

import cn.bitflash.dao.UserDao;
import cn.bitflash.exception.RRException;
import cn.bitflash.login.LoginForm;
import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserEntity;
import cn.bitflash.login.UserGTCidEntity;
import cn.bitflash.service.TokenService;
import cn.bitflash.service.UserGTCidService;
import cn.bitflash.service.UserService;
import cn.bitflash.utils.AES;
import cn.bitflash.utils.AESTokenUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import common.validator.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("userService" )
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserGTCidService userGTCidService;

    @Override
    public UserEntity queryByMobile(String mobile) {
        UserEntity userEntity = new UserEntity();
        userEntity.setMobile(mobile);
        return baseMapper.selectOne(userEntity);
    }


    @Override
    public Map<String, Object> login(LoginForm form) {
        UserEntity user = queryByMobile(form.getMobile());
        Assert.isNull(user, "手机号或密码错误" );

        // 密码错误
        if (!user.getPassword().equals(form.getPassword())) {
            throw new RRException("手机号或密码错误" );
        }

        // 获取登录token
        TokenEntity tokenEntity = tokenService.createToken(user);

        Map<String, Object> map = new HashMap<>(2);
        Long time = System.currentTimeMillis();
        map.put("token", AESTokenUtil.setToken(time.toString(),tokenEntity.getToken()));
        map.put("expire", time);

        UserGTCidEntity gt = userGTCidService.selectOne(new EntityWrapper<UserGTCidEntity>().eq("uid", user.getUid()));

        if (gt == null) {
            gt = new UserGTCidEntity();
            gt.setUid(user.getUid());
            gt.setCid(form.getCid());
            gt.setUpdateTime(new Date());
            userGTCidService.insert(gt);
        } else if (!gt.getCid().equals(form.getCid())) {
            gt.setCid(form.getCid());
            userGTCidService.updateById(gt);
        }


        return map;
    }
}
