package cn.bitflash.service.impl;

import cn.bitflash.dao.UserPayUrlDao;
import cn.bitflash.service.UserPayUrlService;
import cn.bitflash.user.UserPayUrlEntity;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userPayUrlService" )
public class UserPayUrlServiceImpl extends ServiceImpl<UserPayUrlDao, UserPayUrlEntity> implements UserPayUrlService {

}
