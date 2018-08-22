package cn.bitflash.service;

import cn.bitflash.trade.UserBuyHistoryBean;
import cn.bitflash.trade.UserBuyHistoryEntity;
import com.baomidou.mybatisplus.service.IService;

public interface UserBuyHistoryService extends IService<UserBuyHistoryEntity> {

    public UserBuyHistoryBean selectBuyHistory(String id);
}
