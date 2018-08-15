package cn.bitflash.user;

import javax.validation.constraints.NotBlank;

public class ImgForm {

    @NotBlank(message="图片不能为空")
    private String img;

    @NotBlank(message="图片类型不能为空")
    private String imgType;

    private String name;

    private String account;

    @NotBlank(message="交易密码不能为空")
    private String password;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
