package cn.bitflash.feignInterface;

import cn.bitflash.feignInterface.impl.TradeFallback;
import cn.bitflash.trade.UserAccountBean;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.trade.UserTradeEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "bitflash-trade",fallbackFactory = TradeFallback.class)
public interface TradeFeign {

    @PostMapping("/api/trade/inner/selectTradeHistoryIncome")
    Map<String,Object> selectTradeHistoryIncome(@RequestBody  Map<String,Object> param);


    @PostMapping("/api/trade/inner/selectUserAccount")
    UserAccountBean selectUserAccount(@RequestBody  Map<String,Object> param);

    @PostMapping("/api/trade/inner/selectOne")
    UserAccountEntity selectOne(@RequestBody Map<String,Object> map);

    @PostMapping("/api/trade/inner/selectById")
    UserAccountEntity selectById(@RequestParam("uid") String uid);

    @PostMapping("/api/trade/inner/updateById")
    boolean updateById(@RequestBody UserAccountEntity userAccountEntity);

    @PostMapping("/api/trade/inner/selectOneTrade")
    public UserTradeEntity selectOneTrade(@RequestBody Map<String ,Object> map);



}
