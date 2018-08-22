package cn.bitflash.feignInterface;

import cn.bitflash.feignInterface.impl.UserTradeFallback;
import cn.bitflash.trade.UserAccountEntity;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


@FeignClient(value="bitflash-trade",fallbackFactory = UserTradeFallback.class)
public interface UserTradeFeign {

    @PostMapping("/api/trade/inner/insert")
    boolean insert(@RequestBody UserAccountEntity userAccountEntity);

    @PostMapping("/api/trade/inner/selectOne")
    UserAccountEntity selectOne(@RequestBody Map<String,Object> map);
}
