package cn.bitflash.trade;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 申拆表
 * @author wangjun
 */
@TableName("user_complaint" )
public class UserComplaintEntity {

    @TableId(type = IdType.INPUT)
    //订单id
    private Integer orderId;

    //申拆人uid
    private String complaintUid;

    //联系人uid
    private String contactsUid;

    //申拆状态
    private String complaintState;

    //订单状态
    private String orderState;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8" )
    private Date createTime;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getComplaintUid() {
        return complaintUid;
    }

    public void setComplaintUid(String complaintUid) {
        this.complaintUid = complaintUid;
    }

    public String getComplaintState() {
        return complaintState;
    }

    public void setComplaintState(String complaintState) {
        this.complaintState = complaintState;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContactsUid() {
        return contactsUid;
    }

    public void setContactsUid(String contactsUid) {
        this.contactsUid = contactsUid;
    }
}
