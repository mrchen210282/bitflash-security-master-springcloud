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
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户
 *
 * @author wangjun
 */
@TableName("user_trade_history" )
public class UserTradeHistoryEntity implements Serializable {

    private static final long serialVersionUID = -3272015324293238491L;
    /**
     * 交易订单id
     */
    private Integer userTradeId;

    //购买者id
    private String purchaseUid;

    /**
     * 卖出数量
     */
    private BigDecimal purchaseQuantity;

    private String sellUid;

    /**
     * 买入数量
     */
    private BigDecimal sellQuantity;

    /**
     * 0:待确认,1:已完成
     */
    private String state;

    /**
     * 卖出价格
     */
    private BigDecimal price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8" )
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8" )
    private Date finishTime;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(BigDecimal purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public BigDecimal getSellQuantity() {
        return sellQuantity;
    }

    public void setSellQuantity(BigDecimal sellQuantity) {
        this.sellQuantity = sellQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getUserTradeId() {
        return userTradeId;
    }

    public void setUserTradeId(Integer userTradeId) {
        this.userTradeId = userTradeId;
    }

    public String getPurchaseUid() {
        return purchaseUid;
    }

    public void setPurchaseUid(String purchaseUid) {
        this.purchaseUid = purchaseUid;
    }

    public String getSellUid() {
        return sellUid;
    }

    public void setSellUid(String sellUid) {
        this.sellUid = sellUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}
