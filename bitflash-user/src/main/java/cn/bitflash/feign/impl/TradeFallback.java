package cn.bitflash.feign.impl;

import cn.bitflash.feign.TradeFeign;
import cn.bitflash.trade.UserAccountBean;
import cn.bitflash.trade.UserAccountEntity;
import com.baomidou.mybatisplus.mapper.Wrapper;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class TradeFallback implements FallbackFactory<TradeFeign> {

    private static final Logger log=LoggerFactory.getLogger(TradeFallback.class);

    @Override
    public TradeFeign create(Throwable throwable) {
        return new TradeFeign() {
            @Override
            public Map<String, Object> selectTradeHistoryIncome(Map<String, Object> param) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return null;
            }

            @Override
            public UserAccountBean selectUserAccount(Map<String, Object> param) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return new UserAccountBean();
            }

            @Override
            public UserAccountEntity selectOne(Wrapper<UserAccountEntity> param) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return new UserAccountEntity();
            }

            @Override
            public UserAccountEntity selectById(String uid) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return new UserAccountEntity();
            }

            @Override
            public List<Map<String, Object>> selectTradeUrl(Map<String, Object> param) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return null;
            }

            @Override
            public boolean updateById(UserAccountEntity userAccountEntity) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return false;
            }
        };
    }
}
