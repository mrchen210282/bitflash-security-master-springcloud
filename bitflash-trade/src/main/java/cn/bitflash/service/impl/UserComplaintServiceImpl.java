package cn.bitflash.service.impl;

import cn.bitflash.dao.UserComplaintDao;
import cn.bitflash.dao.UserTradeDao;
import cn.bitflash.service.UserComplaintService;
import cn.bitflash.service.UserTradeService;
import cn.bitflash.trade.UserComplaintBean;
import cn.bitflash.trade.UserComplaintEntity;
import cn.bitflash.trade.UserTradeBean;
import cn.bitflash.trade.UserTradeEntity;
import cn.bitflash.utils.Common;
import cn.bitflash.utils.RedisUtils;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 申拆
 * @author wangjun
 * @date 2018年8月16日 下午4:48:48
 */
@Service("userComplaintService" )
public class UserComplaintServiceImpl extends ServiceImpl<UserComplaintDao, UserComplaintEntity> implements UserComplaintService {

    @Override
    public UserComplaintBean getComplaintMessage(String id){
        return baseMapper.getComplaintMessage(id);
    }

}
