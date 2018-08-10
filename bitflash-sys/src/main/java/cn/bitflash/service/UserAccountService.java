package cn.bitflash.service;

import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author soso
 * @date 2018年5月21日 下午4:47:36
 */

public interface UserAccountService extends IService<UserAccountEntity> {

    public void updateUserAccountByParam(UserAccountEntity userAccountEntity);

    public List<UserAccountEntity> selectByMobile(String mobile);

    public UserAccountBean selectUserAccount(Map<String, Object> map);


}
