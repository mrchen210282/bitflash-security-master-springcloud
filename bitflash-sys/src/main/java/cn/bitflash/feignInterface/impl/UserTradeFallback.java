package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.UserTradeFeign;
import cn.bitflash.trade.UserAccountEntity;
import com.baomidou.mybatisplus.mapper.Wrapper;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserTradeFallback implements FallbackFactory<UserTradeFeign> {

    private static final Logger log=LoggerFactory.getLogger(UserTradeFallback.class);

    @Override
    public UserTradeFeign create(Throwable throwable) {
        return new UserTradeFeign() {
            @Override
            public boolean insert(UserAccountEntity userAccountEntity) {
                log.error("注册初始化用户数量失败-----:"+throwable.getMessage());
                return false;
            }

            @Override
            public UserAccountEntity selectOne(Wrapper wrapper) {
                log.error("查询用户数量失败-----:"+throwable.getMessage());
                return new UserAccountEntity();
            }

            @Override
            public boolean update(UserAccountEntity userAccountEntity, Wrapper wrapper) {
                log.error("更新用户数量失败-----:"+throwable.getMessage());
                return false;
            }
        };
    }
}
