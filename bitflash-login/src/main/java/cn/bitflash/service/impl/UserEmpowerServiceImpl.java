package cn.bitflash.service.impl;

import cn.bitflash.dao.UserEmpowerDao;
import cn.bitflash.login.UserEmpowerEntity;
import cn.bitflash.service.UserEmpowerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
*@author wangjun
*@version 2018年7月23日上午11:04:54
*
*/
@Service("userEmpowerService")
public class UserEmpowerServiceImpl extends ServiceImpl<UserEmpowerDao, UserEmpowerEntity> implements UserEmpowerService {

}
