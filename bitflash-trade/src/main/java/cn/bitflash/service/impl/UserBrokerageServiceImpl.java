package cn.bitflash.service.impl;

import cn.bitflash.trade.UserBrokerageEntity;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.bitflash.dao.UserBrokerageDao;
import cn.bitflash.trade.UserBrokerageEntity;
import cn.bitflash.service.UserBrokerageService;

/**
 * @author wangjun
 * @version 2018年7月3日上午11:02:19
 */
@Service("userBrokerageService" )
public class UserBrokerageServiceImpl extends ServiceImpl<UserBrokerageDao, UserBrokerageEntity> implements UserBrokerageService {

}
