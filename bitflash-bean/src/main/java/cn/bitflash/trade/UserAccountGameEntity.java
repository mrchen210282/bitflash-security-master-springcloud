package cn.bitflash.trade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
*@author wangjun
*@version 2018年8月10日下午3:42:36
*
*/
@TableName("user_account_game" )
public class UserAccountGameEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3272015324293238491L;

    /**
     * 用户id
     */
    @TableId(type = IdType.INPUT)
    private String uid;

    /**
     * 购买数量
     */
    private BigDecimal purchase;

    /**
     * 赠送数量
     */
    private BigDecimal giveAmount;

    /**
     * 报单总数 （实际报单数+赠送量）
     */
    private BigDecimal totelAssets;

    /**
     * 调节释放
     */
    private BigDecimal regulateRelease;

    /**
     * 调节收益
     */
    private BigDecimal regulateIncome;

    /**
     * 冻结资产
     */
    private BigDecimal frozenAssets;

    /**
     * 可用资产
     */
    private BigDecimal availableAssets;
    
    /**
     * 左区业绩
     */
    private BigDecimal lftAchievement;

    /**
     * 右区业绩
     */
    private BigDecimal rgtAchievement;

    /**
     * 总收益
     */
    private BigDecimal totelIncome;

    /**
     * 每日收益
     */
    private BigDecimal dailyIncome;

    /**
     * 报单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date createTime;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public BigDecimal getPurchase() {
        return purchase;
    }

    public void setPurchase(BigDecimal purchase) {
        this.purchase = purchase;
    }

    public BigDecimal getGiveAmount() {
        return giveAmount;
    }

    public void setGiveAmount(BigDecimal giveAmount) {
        this.giveAmount = giveAmount;
    }

    public BigDecimal getTotelAssets() {
        return totelAssets;
    }

    public void setTotelAssets(BigDecimal totelAssets) {
        this.totelAssets = totelAssets;
    }

    public BigDecimal getRegulateRelease() {
        return regulateRelease;
    }

    public void setRegulateRelease(BigDecimal regulateRelease) {
        this.regulateRelease = regulateRelease;
    }

    public BigDecimal getRegulateIncome() {
        return regulateIncome;
    }

    public void setRegulateIncome(BigDecimal regulateIncome) {
        this.regulateIncome = regulateIncome;
    }

    public BigDecimal getFrozenAssets() {
        return frozenAssets;
    }

    public void setFrozenAssets(BigDecimal frozenAssets) {
        this.frozenAssets = frozenAssets;
    }

    public BigDecimal getAvailableAssets() {
        return availableAssets;
    }

    public void setAvailableAssets(BigDecimal availableAssets) {
        this.availableAssets = availableAssets;
    }

	public BigDecimal getLftAchievement() {
        return lftAchievement;
    }

    public void setLftAchievement(BigDecimal lftAchievement) {
        this.lftAchievement = lftAchievement;
    }

    public BigDecimal getRgtAchievement() {
        return rgtAchievement;
    }

    public void setRgtAchievement(BigDecimal rgtAchievement) {
        this.rgtAchievement = rgtAchievement;
    }

    public BigDecimal getTotelIncome() {
        return totelIncome;
    }

    public void setTotelIncome(BigDecimal totelIncome) {
        this.totelIncome = totelIncome;
    }

    public BigDecimal getDailyIncome() {
        return dailyIncome;
    }

    public void setDailyIncome(BigDecimal dailyIncome) {
        this.dailyIncome = dailyIncome;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
