package cn.bitflash.user;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

@TableName("user_info_config")
public class UserInfoConfigEntity implements Serializable {

    private static final long serialVersionUID = 7629853906064099813L;
   @TableId(type = IdType.INPUT)
    private Integer id;

   private Integer min;

   private Integer max;

   private Double profit;

   private String showProfit;

   private Double giveRate;

   private String showGiveRate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getGiveRate() {
        return giveRate;
    }

    public void setGiveRate(Double giveRate) {
        this.giveRate = giveRate;
    }

    public String getShowProfit() {
        return showProfit;
    }

    public void setShowProfit(String showProfit) {
        this.showProfit = showProfit;
    }

    public String getShowGiveRate() {
        return showGiveRate;
    }

    public void setShowGiveRate(String showGiveRate) {
        this.showGiveRate = showGiveRate;
    }
}
