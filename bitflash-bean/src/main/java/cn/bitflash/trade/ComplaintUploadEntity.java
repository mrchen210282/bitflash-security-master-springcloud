package cn.bitflash.trade;

import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * 申拆上传图片实体类
 *
 * @author wangjun
 */
@TableName("complaint_upload" )
public class ComplaintUploadEntity implements Serializable {

    //
    private Integer complaintId;

    //上传图片地址
    private String imgUrl;
    //备注
    private String remark;

    public Integer getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Integer complaintId) {
        this.complaintId = complaintId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
