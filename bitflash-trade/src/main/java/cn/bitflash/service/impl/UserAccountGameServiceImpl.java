package cn.bitflash.service.impl;

import cn.bitflash.dao.UserAccountGameDao;
import cn.bitflash.service.UserAccountGameService;
import cn.bitflash.trade.UserAccountGameEntity;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("userAccountGameService" )
public class UserAccountGameServiceImpl extends ServiceImpl<UserAccountGameDao, UserAccountGameEntity> implements UserAccountGameService {

}
