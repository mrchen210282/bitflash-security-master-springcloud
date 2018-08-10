package cn.bitflash.service;

import cn.bitflash.user.UserRelationEntity;
import cn.bitflash.user.UserRelationJoinAccountEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * @author soso
 * @date 2018年5月21日 下午4:47:36
 */

public interface UserRelationService extends IService<UserRelationEntity> {

    public void insertTreeNode(String f_uid, String c_uid, String invitation_code);


    public List<UserRelationJoinAccountEntity> selectTreeNodes(String f_uid);

    public int updateTreeNodes(Integer leftCode, String column);

    public int selectLayer(Integer rgt);

}
