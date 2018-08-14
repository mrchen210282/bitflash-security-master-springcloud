package cn.bitflash.service.impl;

import cn.bitflash.dao.UserBuyHistoryDao;
import cn.bitflash.service.UserBuyHistoryService;
import cn.bitflash.trade.UserBuyHistoryEntity;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("userBuyService" )
public class UserBuyHistoryServiceImpl extends ServiceImpl<UserBuyHistoryDao, UserBuyHistoryEntity> implements UserBuyHistoryService {


}
