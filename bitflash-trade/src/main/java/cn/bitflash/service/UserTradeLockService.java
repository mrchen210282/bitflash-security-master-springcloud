package cn.bitflash.service;

import cn.bitflash.trade.UserTradeLockEntity;
import com.baomidou.mybatisplus.service.IService;

public interface UserTradeLockService extends IService<UserTradeLockEntity> {

    public Integer selectByDay(String uid);

}
