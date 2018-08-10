package cn.bitflash.system;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * @author soso
 * @date 2018年5月22日 下午4:00:45
 */

@TableName("app_status" )
public class AppStatusEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6999516427877319228L;

    @TableId(type = IdType.INPUT)
    private String appid;

    private String version;

    private String url;

    private String note;

    private String title;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
