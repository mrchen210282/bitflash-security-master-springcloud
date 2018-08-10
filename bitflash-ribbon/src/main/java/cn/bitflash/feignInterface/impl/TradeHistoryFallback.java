package cn.bitflash.feignInterface.impl;

import cn.bitflash.feignInterface.TradeHistoryRibbon;
import feign.hystrix.FallbackFactory;

import java.util.Map;

public class TradeHistoryFallback implements FallbackFactory<TradeHistoryRibbon> {
    @Override
    public TradeHistoryRibbon create(Throwable throwable) {
        return new TradeHistoryRibbon() {
            @Override
            public Map<String, Object> selectTradeHistoryIncome(Map<String, Object> map) {
                return null;
            }
        };
    }
}
