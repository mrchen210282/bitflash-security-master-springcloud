package cn.bitflash.dao;

import cn.bitflash.trade.UserBuyHistoryBean;
import cn.bitflash.trade.UserBuyHistoryEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 求购记录
 *
 * @author gaoyuguo
 * @date 2018-8-28 15:22:06
 */
public interface UserBuyHistoryDao extends BaseMapper<UserBuyHistoryEntity> {

    UserBuyHistoryBean selectBuyHistory(String id);

}
