package cn.bitflash.service.impl;

import cn.bitflash.dao.UserBuyHistoryDao;
import cn.bitflash.service.UserBuyHistoryService;
import cn.bitflash.trade.UserBuyHistoryBean;
import cn.bitflash.trade.UserBuyHistoryEntity;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("userBuyHistoryService" )
public class UserBuyHistoryServiceImpl extends ServiceImpl<UserBuyHistoryDao, UserBuyHistoryEntity> implements UserBuyHistoryService {

    @Override
    public UserBuyHistoryBean selectBuyHistory(String id){
        return baseMapper.selectBuyHistory(id);
    }

}
