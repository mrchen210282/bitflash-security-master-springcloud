package cn.bitflash.trade;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 申拆表
 * @author wangjun
 */
@TableName("user_complaint" )
public class UserComplaintBean extends UserComplaintEntity implements Serializable {

    private String mobile;

    private String username;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
