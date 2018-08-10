package cn.bitflash.trade;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

@TableName("user_trade_lock" )
public class UserTradeLockEntity implements Serializable {


    private static final long serialVersionUID = -5320482330364748434L;
    @TableId(type = IdType.INPUT)
    private String userTradeId;
    private String lockUid;
    private Date lockTime;

    public String getUserTradeId() {
        return userTradeId;
    }

    public void setUserTradeId(String userTradeId) {
        this.userTradeId = userTradeId;
    }

    public String getLockUid() {
        return lockUid;
    }

    public void setLockUid(String lockUid) {
        this.lockUid = lockUid;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }
}
