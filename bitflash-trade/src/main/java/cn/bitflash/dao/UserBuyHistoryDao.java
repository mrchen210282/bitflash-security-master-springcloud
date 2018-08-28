package cn.bitflash.dao;

import cn.bitflash.trade.UserBuyHistoryBean;
import cn.bitflash.trade.UserBuyHistoryEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface UserBuyHistoryDao extends BaseMapper<UserBuyHistoryEntity> {

    UserBuyHistoryBean selectBuyHistory(String id);

}
