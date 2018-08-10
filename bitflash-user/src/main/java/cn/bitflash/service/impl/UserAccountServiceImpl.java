package cn.bitflash.service.impl;

import cn.bitflash.dao.UserAccountDao;
import cn.bitflash.service.UserAccountService;
import cn.bitflash.trade.UserAccountBean;
import cn.bitflash.trade.UserAccountEntity;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author soso
 * @date 2018年5月21日 下午4:48:48
 */

@Service("userAccountService" )
public class UserAccountServiceImpl extends ServiceImpl<UserAccountDao, UserAccountEntity> implements UserAccountService {

    public void updateUserAccountByParam(UserAccountEntity userAccount) {
        baseMapper.updateUserAccountByParam(userAccount);
    }


    @Override
    public List<UserAccountEntity> selectByMobile(String mobile) {
        return baseMapper.selectByMobile(mobile);
    }

    public UserAccountBean selectUserAccount(Map<String, Object> map) {
        return baseMapper.selectUserAccount(map);
    }
}
