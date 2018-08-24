package cn.bitflash.tradeutil;

import cn.bitflash.trade.UserAccountBean;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.trade.UserBuyEntity;
import cn.bitflash.trade.UserTradeEntity;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class TradeFallback implements FallbackFactory<TradeUtils> {

    private static final Logger log=LoggerFactory.getLogger(TradeFallback.class);

    @Override
    public TradeUtils create(Throwable throwable) {
        return new TradeUtils() {
            @Override
            public Map<String, Object> selectTradeHistoryIncome(Map<String, Object> param) {
                log.error("获取地址失败-----:"+throwable.getMessage());
                return null;
            }

            @Override
            public UserAccountBean selectUserAccount(Map<String, Object> param) {
                log.error("获取用户账号失败-----:"+throwable.getMessage());
                return new UserAccountBean();
            }


            @Override
            public UserAccountEntity selectOne(Map<String,Object> map) {
                log.error("获取账号失败-----:"+throwable.getMessage());
                return new UserAccountEntity();
            }

            @Override
            public UserAccountEntity selectById(String uid) {
                log.error("获取账号失败-----:"+throwable.getMessage());
                return new UserAccountEntity();
            }

            @Override
            public boolean updateById(UserAccountEntity userAccountEntity) {
                log.error("修改账号失败-----:"+throwable.getMessage());
                return false;
            }

            @Override
            public UserTradeEntity selectOneTrade(Map<String, Object> map) {
                log.error("selectOneTrade-----:"+throwable.getMessage());
                return null;
            }

            @Override
            public UserBuyEntity selectOneBuy(Map<String, Object> map) {
                log.error("selectOneBuy-----:"+throwable.getMessage());
                return null;
            }
        };
    }
}
