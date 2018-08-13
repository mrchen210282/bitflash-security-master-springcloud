package cn.bitflash.service.impl;

import cn.bitflash.dao.UserInfoDao;
import cn.bitflash.service.UserInfoService;
import cn.bitflash.user.UserInfoEntity;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Service("userInfoService" )
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfoEntity> implements UserInfoService {


}
