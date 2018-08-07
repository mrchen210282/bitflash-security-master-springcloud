package cn.bitflash.user;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

@TableName("user_info" )
public class UserInfoEntity implements Serializable {

    private static final long serialVersionUID = 4282111755160371079L;

    //用户ID
    @TableId(type = IdType.INPUT)
    private String uid;
    //名字
    private String realname;
    //昵称
    private String nickname;
    //昵称锁定
    private String nicklock;
    //身份证号
    private String idNumber;
    //手机号
    private String mobile;
    //vip升级时间
    private Date vipCreateTime;

    private Date authenticationTime;

    //vip等级（0,1,2）
    private String isVip;

    //推广码判断字段
    private Boolean isInvitation;

    //实名注册(0未认证,1认证中,2认证完成)
    private String isAuthentication;

    private String invitationCode;

    public String getNicklock() {
        return nicklock;
    }

    public void setNicklock(String nicklock) {
        this.nicklock = nicklock;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Boolean getIsInvitation() {
        return isInvitation;
    }

    public void setIsInvitation(Boolean isInvitation) {
        this.isInvitation = isInvitation;
    }

    public String getIsVip() {
        return isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public Boolean getInvitation() {
        return isInvitation;
    }

    public void setInvitation(Boolean invitation) {
        isInvitation = invitation;
    }

    public String getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(String isAuthentication) {
        this.isAuthentication = isAuthentication;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getVipCreateTime() {
        return vipCreateTime;
    }

    public void setVipCreateTime(Date vipCreateTime) {
        this.vipCreateTime = vipCreateTime;
    }

    public Date getAuthenticationTime() {
        return authenticationTime;
    }

    public void setAuthenticationTime(Date authenticationTime) {
        this.authenticationTime = authenticationTime;
    }
}