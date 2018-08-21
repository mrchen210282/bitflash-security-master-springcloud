package cn.bitflash.service;

import cn.bitflash.trade.UserComplaintBean;
import cn.bitflash.trade.UserComplaintEntity;
import com.baomidou.mybatisplus.service.IService;

/**
 * @author wangjun
 * @date 2018年8月16日 下午4:47:36
 */
public interface UserComplaintService extends IService<UserComplaintEntity> {

    public UserComplaintBean getComplaintMessage(String id);

}
