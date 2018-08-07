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

package cn.bitflash.user;

import java.io.Serializable;

/**
 * 用户
 *
 * @author eric
 */
public class UserAccountBean extends UserAccountEntity implements Serializable  {


    private static final long serialVersionUID = -3272015324293238491L;

    /**
     * 是否已修改过昵称
     */
    private String nicklock;

    public String getNicklock() {
        return nicklock;
    }

    public void setNicklock(String nicklock) {
        this.nicklock = nicklock;
    }


}
