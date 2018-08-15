package cn.bitflash.service;


import cn.bitflash.trade.UserAccountBean;
import cn.bitflash.trade.UserAccountEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author soso
 * @date 2018年5月21日 下午4:47:36
 */

public interface UserAccountService extends IService<UserAccountEntity> {

    public void updateUserAccountByParam(UserAccountEntity userAccountEntity);

    public UserAccountBean selectUserAccount(Map<String, Object> map);

}
