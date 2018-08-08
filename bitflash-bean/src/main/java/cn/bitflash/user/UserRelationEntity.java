package cn.bitflash.user;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 用户体系
 *
 * @author soso
 * @date 2018年5月26日 上午10:36:57
 */

@TableName("user_relation" )
public class UserRelationEntity {


    /**
     * 用户id
     */
    @TableId(type = IdType.INPUT)
    private String uid;


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
     * 层
     */
    private Integer layer;

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

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }
}
