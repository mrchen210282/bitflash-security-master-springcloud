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

import cn.bitflash.trade.UserTradeEntity;
import com.baomidou.mybatisplus.annotations.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户
 *
 * @author wangjun
 */
public class UserTradeBean extends UserTradeEntity implements Serializable {

    private static final long serialVersionUID = -3272015324293238491L;

    private String realname;

    /**
     * 昵称
     */
    private String nickname;

    private String mobile;


    /**
     * 卖出(1)、购买(2),撤消(3)
     */
    private String state;

    /**
     * 报单总数 （实际报单数+赠送量）
     */
    private BigDecimal totelAssets;

    //是否为自己卖出(1),不是自己则为空
    private String isMySelf;

    /**
     * 购买人姓名
     */
    private String purchaseName;

    /**
     * 购买人手机
     */
    private String purchaseMobile;

    /**
     * 卖出人姓名
     */
    private String sellName;

    //手续费
    private BigDecimal poundage;

    //交易总额
    private BigDecimal tradeAmount;

    //扣除交易额=交易额+手续费
    private BigDecimal deductAmount;



    //********完成************
    /**
     * 发单人信息
     */
    private String purUid;

    private String purName;

    private String purMobile;
    /**
     * 应单人信息
     */
    private String conUid;

    private String conName;

    private String conMobile;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 价钱
     */
    private String acprice;

    /**
     * 数量
     */
    private String acquantity;

    private String tradeState;

    public String getPurUid() {
        return purUid;
    }

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public void setPurUid(String purUid) {
        this.purUid = purUid;
    }

    public String getConUid() {
        return conUid;
    }

    public void setConUid(String conUid) {
        this.conUid = conUid;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

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

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getTotelAssets() {
        return totelAssets;
    }

    public void setTotelAssets(BigDecimal totelAssets) {
        this.totelAssets = totelAssets;
    }

    public String getIsMySelf() {
        return isMySelf;
    }

    public void setIsMySelf(String isMySelf) {
        this.isMySelf = isMySelf;
    }

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

    public String getSellName() {
        return sellName;
    }

    public void setSellName(String sellName) {
        this.sellName = sellName;
    }

    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public BigDecimal getDeductAmount() {
        return deductAmount;
    }

    public void setDeductAmount(BigDecimal deductAmount) {
        this.deductAmount = deductAmount;
    }

    public String getPurName() {
        return purName;
    }

    public void setPurName(String purName) {
        this.purName = purName;
    }

    public String getPurMobile() {
        return purMobile;
    }

    public void setPurMobile(String purMobile) {
        this.purMobile = purMobile;
    }

    public String getConName() {
        return conName;
    }

    public void setConName(String conName) {
        this.conName = conName;
    }

    public String getConMobile() {
        return conMobile;
    }

    public void setConMobile(String conMobile) {
        this.conMobile = conMobile;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAcprice() {
        return acprice;
    }

    public void setAcprice(String acprice) {
        this.acprice = acprice;
    }

    public String getAcquantity() {
        return acquantity;
    }

    public void setAcquantity(String acquantity) {
        this.acquantity = acquantity;
    }
}
