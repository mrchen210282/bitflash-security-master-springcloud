package cn.bitflash.service.impl;

import cn.bitflash.dao.AuthorityUserDao;
import cn.bitflash.login.AuthorityUserEntity;
import cn.bitflash.service.AuthorityUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author wangjun
 * @version 2018年8月1日下午1:32:59
 *
 */
@Service("authorityUserService" )
public class AuthorityUserServiceImpl extends ServiceImpl<AuthorityUserDao, AuthorityUserEntity> implements AuthorityUserService {
	
}
