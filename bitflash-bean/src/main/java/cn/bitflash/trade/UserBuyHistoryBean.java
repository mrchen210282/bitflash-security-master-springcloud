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

/**
 * 求购记录
 *
 * @author gaoyuguo
 */
public class UserBuyHistoryBean extends UserBuyHistoryEntity implements Serializable {

    private String nickname;

    private String mobile;

    private String purUname;

    private String sellUname;

    private String purMobile;

    private String sellMobile;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPurUname() {
        return purUname;
    }

    public void setPurUname(String purUname) {
        this.purUname = purUname;
    }

    public String getSellUname() {
        return sellUname;
    }

    public void setSellUname(String sellUname) {
        this.sellUname = sellUname;
    }

    public String getPurMobile() {
        return purMobile;
    }

    public void setPurMobile(String purMobile) {
        this.purMobile = purMobile;
    }

    public String getSellMobile() {
        return sellMobile;
    }

    public void setSellMobile(String sellMobile) {
        this.sellMobile = sellMobile;
    }
}
