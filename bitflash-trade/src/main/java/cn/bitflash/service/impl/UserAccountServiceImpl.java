package cn.bitflash.service.impl;

import java.util.List;
import java.util.Map;

import cn.bitflash.user.UserAccountBean;
import cn.bitflash.user.UserAccountEntity;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.bitflash.dao.UserAccountDao;
import cn.bitflash.service.UserAccountService;

/**
 * @author soso
 * @date 2018年5月21日 下午4:48:48
 */

@Service("userAccountService" )
public class UserAccountServiceImpl extends ServiceImpl<UserAccountDao, UserAccountEntity> implements UserAccountService {

    @Override
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
