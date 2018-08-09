package cn.bitflash.trade;

import java.io.Serializable;

public class UserBuyMessageBean extends UserBuyEntity implements Serializable {

    private static final long serialVersionUID = 4506762769296158220L;

    private String isMy;

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIsMy() {
        return isMy;
    }

    public void setIsMy(String isMy) {
        this.isMy = isMy;
    }
}
