package cn.bitflash.dao;

import java.util.List;
import java.util.Map;

import cn.bitflash.trade.UserTradeHistoryBean;
import cn.bitflash.trade.UserTradeHistoryEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.bitflash.trade.UserTradeHistoryBean;
import cn.bitflash.trade.UserTradeHistoryEntity;

/**
 * @author wangjun
 * @date 2018年6月19日 下午4:45:51
 */
public interface UserTradeHistoryDao extends BaseMapper<UserTradeHistoryEntity> {

    public void updateUserTradeHistory(UserTradeHistoryEntity uesrTradeHistory);

    public List<UserTradeHistoryBean> selectTradeHistory(Map<String, Object> map);

    public Map<String, Object> selectTradeHistoryIncome(Map<String, Object> map);

    public void insertUserTradeHistory(UserTradeHistoryEntity uesrTradeHistory);
}