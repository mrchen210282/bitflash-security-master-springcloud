package cn.bitflash.dao;

import cn.bitflash.trade.UserBuyBean;
import cn.bitflash.trade.UserBuyHistoryBean;
import cn.bitflash.trade.UserBuyHistoryEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface UserBuyHistoryDao extends BaseMapper<UserBuyHistoryEntity> {

    //public List<UserBuyBean> selectBuyList(String uid);

    public UserBuyHistoryBean selectBuyHistory(String id);

}
