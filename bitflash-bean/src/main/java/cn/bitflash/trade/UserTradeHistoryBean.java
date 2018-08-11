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

package cn.bitflash.trade;

import java.io.Serializable;
import java.util.Date;

import cn.bitflash.trade.UserTradeHistoryEntity;
import com.baomidou.mybatisplus.annotations.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户
 *
 * @author wangjun
 */
public class UserTradeHistoryBean extends UserTradeHistoryEntity implements Serializable {

    private static final long serialVersionUID = -3272015324293238491L;

    private String purchaseName;

    private String purchaseMobile;

    private String nickname;

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public String getPurchaseMobile() {
        return purchaseMobile;
    }

    public void setPurchaseMobile(String purchaseMobile) {
        this.purchaseMobile = purchaseMobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
