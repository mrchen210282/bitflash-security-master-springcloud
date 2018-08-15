package cn.bitflash.dao;

import cn.bitflash.trade.UserAccountBean;
import cn.bitflash.trade.UserAccountEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author soso
 * @date 2018年5月21日 下午4:45:51
 */
@Repository
public interface UserAccountDao extends BaseMapper<UserAccountEntity> {

    public void updateUserAccountByParam(UserAccountEntity userAccountEntity);

    public UserAccountBean selectUserAccount(Map<String, Object> map);

}
