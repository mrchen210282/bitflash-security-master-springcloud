package cn.bitflash.trade;

import java.io.Serializable;

/**
 * 申拆表
 *
 * @author wangjun
 */
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
