package cn.bitflash.tradeutil;

import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.trade.UserAccountGameEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


@FeignClient(value="bitflash-trade",fallbackFactory = UserTradeFallback.class)
public interface TradeUtils {

    @PostMapping("/api/trade/inner/insert")
    boolean insert(@RequestBody UserAccountEntity userAccountEntity);

    @PostMapping("/api/trade/inner/selectOne")
    UserAccountEntity selectOne(@RequestBody Map<String,Object> map);

    @PostMapping("/api/trade/inner/insertUserAccountGame")
    void insertUserAccountGame(UserAccountGameEntity userAccountGameEntity);

}
