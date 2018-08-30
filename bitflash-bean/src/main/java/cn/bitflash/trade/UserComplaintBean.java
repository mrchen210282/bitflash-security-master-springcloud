package cn.bitflash.trade;

import java.io.Serializable;

/**
 * 申诉
 *
 * @author gaoyuguo
 */
public class UserComplaintBean extends UserComplaintEntity implements Serializable {

    private String contactsUname;

    private String contactsMobile;

    private String complaintUname;

    private String complaintMobile;

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

    public String getComplaintUname() {
        return complaintUname;
    }

    public void setComplaintUname(String complaintUname) {
        this.complaintUname = complaintUname;
    }

    public String getComplaintMobile() {
        return complaintMobile;
    }

    public void setComplaintMobile(String complaintMobile) {
        this.complaintMobile = complaintMobile;
    }
}
