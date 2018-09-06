package cn.bitflash.service;

import java.util.List;
import java.util.Map;

import cn.bitflash.trade.UserTradeHistoryEntity;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;

import cn.bitflash.trade.UserTradeHistoryBean;
import cn.bitflash.trade.UserTradeHistoryEntity;

/**
 * @author wangjun
 * @date 2018年6月19日 下午4:47:36
 */
@Service("userTradeHistoryService" )
public interface UserTradeHistoryService extends IService<UserTradeHistoryEntity> {

    public void updateUserTradeHistory(UserTradeHistoryEntity uesrTradeHistory);

    public List<UserTradeHistoryBean> selectTradeHistory(Map<String, Object> map);

    public Map<String, Object> selectTradeHistoryIncome(Map<String, Object> map);

    public void insertUserTradeHistory(UserTradeHistoryEntity uesrTradeHistory);
}
