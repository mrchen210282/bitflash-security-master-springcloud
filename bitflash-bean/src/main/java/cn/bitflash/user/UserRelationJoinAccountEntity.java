package cn.bitflash.user;

import java.math.BigDecimal;

/**
 * 用户体系合并用户信息
 *
 * @author soso
 * @date 2018年5月26日 上午10:36:57
 */
public class UserRelationJoinAccountEntity {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 用户姓名
     */
    private String realname;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 直推邀请码
     */
    private String invitation_code;

    /**
     * 左边界值
     */
    private Integer lft;

    /**
     * 右边界值
     */
    private Integer rgt;

    /**
     * 节点层次
     */
    private Integer layer;

    /**
     * 报单总数 （实际报单数+赠送量）
     */
    private BigDecimal totelAssets;

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
     * 获取用户id
     *
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置用户id
     *
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * 获取用户姓名
     *
     * @return realname
     */
    public String getRealname() {
        return realname;
    }

    /**
     * 设置用户姓名
     *
     * @param realname
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * 获取身份证号
     *
     * @return idNumber
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * 设置身份证号
     *
     * @param idNumber
     */
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * 获取手机号
     *
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取直推邀请码
     *
     * @return invitation_code
     */
    public String getInvitation_code() {
        return invitation_code;
    }

    /**
     * 设置直推邀请码
     *
     * @param invitation_code
     */
    public void setInvitation_code(String invitation_code) {
        this.invitation_code = invitation_code;
    }

    /**
     * 获取左边界值
     *
     * @return lft
     */
    public Integer getLft() {
        return lft;
    }

    /**
     * 设置左边界值
     *
     * @param lft
     */
    public void setLft(Integer lft) {
        this.lft = lft;
    }

    /**
     * 获取右边界值
     *
     * @return rgt
     */
    public Integer getRgt() {
        return rgt;
    }

    /**
     * 设置右边界值
     *
     * @param rgt
     */
    public void setRgt(Integer rgt) {
        this.rgt = rgt;
    }

    /**
     * 获取节点层次
     *
     * @return layer
     */
    public Integer getLayer() {
        return layer;
    }

    /**
     * 设置节点层次
     *
     * @param layer
     */
    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    /**
     * 获取报单总数
     *
     * @return totelAssets
     */
    public BigDecimal getTotelAssets() {
        return totelAssets;
    }

    /**
     * 设置报单总数
     *
     * @param totelAssets
     */
    public void setTotelAssets(BigDecimal totelAssets) {
        this.totelAssets = totelAssets;
    }

    /**
     * 获取冻结资产
     *
     * @return frozenAssets
     */
    public BigDecimal getFrozenAssets() {
        return frozenAssets;
    }

    /**
     * 设置冻结资产
     *
     * @param frozenAssets
     */
    public void setFrozenAssets(BigDecimal frozenAssets) {
        this.frozenAssets = frozenAssets;
    }

    /**
     * 获取可用资产
     *
     * @return availableAssets
     */
    public BigDecimal getAvailableAssets() {
        return availableAssets;
    }

    /**
     * 设置可用资产
     *
     * @param availableAssets
     */
    public void setAvailableAssets(BigDecimal availableAssets) {
        this.availableAssets = availableAssets;
    }

    /**
     * 获取左区业绩
     *
     * @return lftAchievement
     */
    public BigDecimal getLftAchievement() {
        return lftAchievement;
    }

    /**
     * 设置左区业绩
     *
     * @param lftAchievement
     */
    public void setLftAchievement(BigDecimal lftAchievement) {
        this.lftAchievement = lftAchievement;
    }

    /**
     * 获取右区业绩
     *
     * @return rgtAchievement
     */
    public BigDecimal getRgtAchievement() {
        return rgtAchievement;
    }

    /**
     * 设置右区业绩
     *
     * @param rgtAchievement
     */
    public void setRgtAchievement(BigDecimal rgtAchievement) {
        this.rgtAchievement = rgtAchievement;
    }

}
