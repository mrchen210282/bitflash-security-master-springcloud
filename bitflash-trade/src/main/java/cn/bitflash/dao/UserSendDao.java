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

package cn.bitflash.dao;

import cn.bitflash.trade.UserSendEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import feign.Param;

import java.util.List;


/**
 * 用户发送
 *
 * @author eric
 */
public interface UserSendDao extends BaseMapper<UserSendEntity> {

    List<UserSendEntity> selectaccount(@Param("send_uid") String send_uid, @Param("pages") Integer pages);

    List<UserSendEntity> selectaccept(@Param("send_uid") String send_uid, @Param("pages") Integer pages);

    Integer selectaccountcount(String send_uid);

    Integer selectacceptcount(String send_uid);

}
