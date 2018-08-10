package cn.bitflash.feignInterface;

import cn.bitflash.feignInterface.impl.TradeFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value="bitflash-trade",fallbackFactory = TradeFeignFallback.class)
public interface BitflashTradeFeign {
}
