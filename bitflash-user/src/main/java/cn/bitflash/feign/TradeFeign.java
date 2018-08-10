package cn.bitflash.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(value = "bitflash-trade")
public interface TradeFeign {

    @PostMapping("/api/user-app/logout")
    public Map<String,Object> selectTradeHistoryIncome(Map<String,Object> param);

}
