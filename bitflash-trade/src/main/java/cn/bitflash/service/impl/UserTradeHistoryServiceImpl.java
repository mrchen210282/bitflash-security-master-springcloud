package cn.bitflash.service.impl;

import java.util.List;
import java.util.Map;

import cn.bitflash.trade.UserTradeHistoryBean;
import cn.bitflash.trade.UserTradeHistoryEntity;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.bitflash.dao.UserTradeHistoryDao;
import cn.bitflash.trade.UserTradeHistoryBean;
import cn.bitflash.trade.UserTradeHistoryEntity;
import cn.bitflash.service.UserTradeHistoryService;

/**
 * @author wangjun
 * @date 2018年5月21日 下午4:48:48
 */
@Service("userTradeHistoryService" )
public class UserTradeHistoryServiceImpl extends ServiceImpl<UserTradeHistoryDao, UserTradeHistoryEntity> implements UserTradeHistoryService {

    public void updateUserTradeHistory(UserTradeHistoryEntity uesrTradeHistory) {
        baseMapper.updateUserTradeHistory(uesrTradeHistory);
    }

    public List<UserTradeHistoryBean> selectTradeHistory(Map<String, Object> map) {
        List<UserTradeHistoryBean> list = baseMapper.selectTradeHistory(map);
        return list;
    }

    public Map<String, Object> selectTradeHistoryIncome(Map<String, Object> map) {
        Map<String, Object> returnMap = baseMapper.selectTradeHistoryIncome(map);
        return returnMap;
    }

    public void insertUserTradeHistory(UserTradeHistoryEntity uesrTradeHistory) {
        baseMapper.insertUserTradeHistory(uesrTradeHistory);
    }

}
