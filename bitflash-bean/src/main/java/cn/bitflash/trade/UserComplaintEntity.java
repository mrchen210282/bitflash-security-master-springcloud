package cn.bitflash.trade;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 申拆表
 * @author wangjun
 */
@TableName("user_complaint" )
public class UserComplaintEntity {

    //订单id
    private Integer userTradeId;

    //申拆人uid
    private String complaintUid;

    //申拆状态
    private Integer complaintState;

    //订单状态
    private Integer orderState;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8" )
    private Date createTime;

    public Integer getUserTradeId() {
        return userTradeId;
    }

    public void setUserTradeId(Integer userTradeId) {
        this.userTradeId = userTradeId;
    }

    public String getComplaintUid() {
        return complaintUid;
    }

    public void setComplaintUid(String complaintUid) {
        this.complaintUid = complaintUid;
    }

    public Integer getComplaintState() {
        return complaintState;
    }

    public void setComplaintState(Integer complaintState) {
        this.complaintState = complaintState;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
