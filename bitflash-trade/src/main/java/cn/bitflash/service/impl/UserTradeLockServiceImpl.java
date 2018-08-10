package cn.bitflash.service.impl;

import cn.bitflash.trade.UserTradeLockEntity;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.bitflash.dao.UserTradeLockDao;
import cn.bitflash.service.UserTradeLockService;

@Service("userTradeLockService" )
public class UserTradeLockServiceImpl extends ServiceImpl<UserTradeLockDao, UserTradeLockEntity> implements UserTradeLockService {

    @Override
    public Integer selectByDay(String uid) {
        return baseMapper.selectByDay(uid);
    }
}
