package cn.bitflash.feignInterface;

import cn.bitflash.feignInterface.impl.TradeHistoryFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * wangjun
 */
@FeignClient(value="bitflash-tradeHistory",fallbackFactory = TradeHistoryFallback.class)
public interface TradeHistoryRibbon {

    @PostMapping("/api/tradeHistory/selectTradeHistoryIncome")
    public Map<String, Object> selectTradeHistoryIncome(Map<String, Object> map);
}
