package cn.bitflash.service.impl;

import cn.bitflash.dao.UserInfoConfigDao;
import cn.bitflash.service.UserInfoConfigService;
import cn.bitflash.user.UserInfoConfigEntity;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("userInfoConfigService")
public class UserInfoConfigServiceImpl extends ServiceImpl<UserInfoConfigDao,UserInfoConfigEntity> implements UserInfoConfigService {
}
