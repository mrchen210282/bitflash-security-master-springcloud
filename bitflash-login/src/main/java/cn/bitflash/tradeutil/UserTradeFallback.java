package cn.bitflash.tradeutil;

import cn.bitflash.trade.UserAccountGameEntity;
import cn.bitflash.tradeutil.TradeUtils;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.userutil.UserFeignFallback;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserTradeFallback implements FallbackFactory<TradeUtils> {

    private static final Logger log=LoggerFactory.getLogger(UserFeignFallback.class);

    @Override
    public TradeUtils create(Throwable throwable) {
        return new TradeUtils() {
            @Override
            public boolean insert(UserAccountEntity userAccountEntity) {
                log.error("注册初始化用户数量失败-----:"+throwable.getMessage());
                return false;
            }

            @Override
            public UserAccountEntity selectOne(Map<String, Object> map) {
                log.error("查询用户数量失败-----:"+throwable.getMessage());
                return new UserAccountEntity();
            }

            @Override
            public void insertUserAccountGame(UserAccountGameEntity userAccountGameEntity) {
                log.error("查询用户数量失败-----:"+throwable.getMessage());
            }
        };
    }
}
