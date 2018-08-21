package cn.bitflash.dao;

import cn.bitflash.trade.UserComplaintBean;
import cn.bitflash.trade.UserComplaintEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @author wangjun
 * @date 2018年8月16日 下午4:45:51
 */
public interface UserComplaintDao extends BaseMapper<UserComplaintEntity> {
    public UserComplaintBean getComplaintMessage(String id);

}