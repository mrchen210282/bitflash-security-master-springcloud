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

package cn.bitflash.service;

import cn.bitflash.trade.UserSendEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;


/**
 * 用户发送
 *
 * @author gaoyuguo
 * @date 2018-8-28 15:22:06
 */
public interface UserSendService extends IService<UserSendEntity> {

    /**
     * 查询发送记录
     *
     * @param send_uid 发送人id
     * @param pages    分页
     * @return
     */
    List<UserSendEntity> selectaccount(String send_uid, Integer pages);

    /**
     * 查询接收记录
     *
     * @param send_uid 接收人id
     * @param pages    分页
     * @return
     */
    List<UserSendEntity> selectaccept(String send_uid, Integer pages);

    /**
     * 查询发送记录数量
     *
     * @param send_uid 发送人id
     * @return
     */
    Integer selectaccountcount(String send_uid);

    /**
     * 查询接收记录数量
     *
     * @param send_uid 接收人id
     * @return
     */
    Integer selectacceptcount(String send_uid);
}
