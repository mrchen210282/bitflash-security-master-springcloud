package cn.bitflash.user;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * 用户邀请码
 *
 * @author soso
 * @date 2018年5月22日 下午10:44:03
 */

@TableName("user_invitation_code" )
public class UserInvitationCodeEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -672573899381849255L;

    @TableId(type = IdType.INPUT)
    private String uid;

    private String lftCode;

    private String rgtCode;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLftCode() {
        return lftCode;
    }

    public void setLftCode(String lftCode) {
        this.lftCode = lftCode;
    }

    public String getRgtCode() {
        return rgtCode;
    }

    public void setRgtCode(String rgtCode) {
        this.rgtCode = rgtCode;
    }

}
