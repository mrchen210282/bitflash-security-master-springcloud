package cn.bitflash.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.bitflash.entity.UserAccountBean;
import cn.bitflash.entity.UserAccountEntity;

/**
 * @author soso
 * @date 2018年5月21日 下午4:45:51
 */
@Repository
public interface UserAccountDao extends BaseMapper<UserAccountEntity> {

    public void updateUserAccountByParam(UserAccountEntity userAccountEntity);

    public List<UserAccountEntity> selectByMobile(String mobile);

    public UserAccountBean selectUserAccount(Map<String, Object> map);

}
