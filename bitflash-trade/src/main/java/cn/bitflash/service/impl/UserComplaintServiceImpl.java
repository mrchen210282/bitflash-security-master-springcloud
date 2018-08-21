package cn.bitflash.service.impl;

import cn.bitflash.dao.UserComplaintDao;
import cn.bitflash.service.UserComplaintService;
import cn.bitflash.trade.UserComplaintBean;
import cn.bitflash.trade.UserComplaintEntity;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
