package cn.bitflash.trade;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 游戏贝壳记录
 *
 * @author wangjun
 */
@TableName("game_account_history" )
public class GameAccountHistoryEntity implements Serializable {

    @TableId
    private Integer id;

    //用户尖
    private String uid;

    //数量
    private BigDecimal quantity;

    //0:添加,1:减少
    private String flag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
