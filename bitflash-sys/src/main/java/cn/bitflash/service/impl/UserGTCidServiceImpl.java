package cn.bitflash.service.impl;

import cn.bitflash.dao.UserGTCidDao;
import cn.bitflash.service.UserGTCidService;
import cn.bitflash.user.UserGTCidEntity;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("userGTCidService" )
public class UserGTCidServiceImpl extends ServiceImpl<UserGTCidDao, UserGTCidEntity> implements UserGTCidService {
}
