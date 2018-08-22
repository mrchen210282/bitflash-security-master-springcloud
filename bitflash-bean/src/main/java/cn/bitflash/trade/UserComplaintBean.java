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

    private String contactsUname;
    private String contactsMobile;

    public String getContactsUname() {
        return contactsUname;
    }

    public void setContactsUname(String contactsUname) {
        this.contactsUname = contactsUname;
    }

    public String getContactsMobile() {
        return contactsMobile;
    }

    public void setContactsMobile(String contactsMobile) {
        this.contactsMobile = contactsMobile;
    }
}
