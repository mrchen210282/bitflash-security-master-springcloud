package cn.bitflash.trade;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * wangjun
 * 交易和求购
 */
public class UserTradeJoinBuyEntity {

    private Integer orderId;

    //价格
    private float price;

    //数量
    private float quantity;

    //0:卖入  1:卖出
    private String tradeState;

    //昵称
    private String nickname;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone="GMT+8" )
    private Date finishTime;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}
