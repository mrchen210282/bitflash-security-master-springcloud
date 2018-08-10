package cn.bitflash.feign;

import cn.bitflash.DefaultFallBack.DefaultFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * wangjun
 */
@FeignClient(value="bitflash-tradeHistory",fallbackFactory = DefaultFallBackFactory.class)
public interface TradeHistoryRibbon {

    @PostMapping("/api/tradeHistory/selectTradeHistoryIncome")
    public Map<String, Object> selectTradeHistoryIncome(Map<String, Object> map);
}
