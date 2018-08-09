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


import cn.bitflash.dao.UserSendDao;
import cn.bitflash.entity.UserSendEntity;
import cn.bitflash.service.UserSendService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userSendService" )
public class UserSendServiceImpl extends ServiceImpl<UserSendDao, UserSendEntity> implements UserSendService {

    @Override
    public List<UserSendEntity> selectaccount(String send_uid) {

        return baseMapper.selectaccount(send_uid);
    }

    @Override
    public List<UserSendEntity> selectaccept(String send_uid) {

        return baseMapper.selectaccept(send_uid);
    }

}
