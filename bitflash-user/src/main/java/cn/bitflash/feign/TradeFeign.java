package cn.bitflash.feign;

import cn.bitflash.feign.impl.TradeFallback;
import cn.bitflash.trade.UserAccountBean;
import cn.bitflash.trade.UserAccountEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@FeignClient(value = "bitflash-trade",fallbackFactory = TradeFallback.class)
public interface TradeFeign {

    @PostMapping("/api/trade/selectTradeHistoryIncome")
    Map<String,Object> selectTradeHistoryIncome(Map<String,Object> param);

    @PostMapping("/api/trade/selectUserAccount")
    UserAccountBean selectUserAccount(Map<String,Object> param);

    @PostMapping("/api/trade/selectOne")
    UserAccountEntity selectOne(Map<String,Object> map);

    @PostMapping("/api/trade/selectById")
    UserAccountEntity selectById(String uid);

    @PostMapping("/api/trade/selectTradeUrl")
    List<Map<String, Object>> selectTradeUrl(Map<String,Object> param);

    @PostMapping("/api/trade/updateById")
    boolean updateById(UserAccountEntity userAccountEntity);


}
