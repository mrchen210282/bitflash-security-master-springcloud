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

import cn.bitflash.dao.TokenDao;
import cn.bitflash.service.TokenService;
import cn.bitflash.user.TokenEntity;
import cn.bitflash.user.UserEntity;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service("tokenService" )
public class TokenServiceImpl extends ServiceImpl<TokenDao, TokenEntity> implements TokenService {
    /**
     * 一个星期后过期
     */
    private final static int EXPIRE = 90000 * 1200;

    @Override
    public TokenEntity queryByToken(String mobile) {
        return this.selectOne(new EntityWrapper<TokenEntity>().eq("mobile", mobile));
    }

    @Override
    public TokenEntity createToken(UserEntity user) {
        // 当前时间
        Date now = new Date();
        // 过期时间
        Date expireTime = new Date(now.getTime() + 1000 * 60 * 60 * 24 * 7);

        // 生成token
        String token = generateToken();

        // 保存或更新用户token
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUid(user.getUid());
        tokenEntity.setMobile(user.getMobile());
        tokenEntity.setToken(token);
        tokenEntity.setUpdateTime(now);
        tokenEntity.setExpireTime(expireTime);
        this.insertOrUpdate(tokenEntity);
        return tokenEntity;
    }

    @Override
    public void expireToken(String uid) {
        Date now = new Date();

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUid(uid);
        tokenEntity.setUpdateTime(now);
        tokenEntity.setExpireTime(now);
        this.insertOrUpdate(tokenEntity);
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "" );
    }
}
