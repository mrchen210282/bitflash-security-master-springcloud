package cn.bitflash.feignInterface;

import cn.bitflash.feignInterface.impl.UserTradeFallback;
import cn.bitflash.trade.UserAccountEntity;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value="bitflsh-trade",fallbackFactory = UserTradeFallback.class)
public interface UserTradeFeign {

    @PostMapping("/api/trade/withinAccount/insert")
    boolean insert(@RequestBody UserAccountEntity userAccountEntity);

    @PostMapping("/api/trade/withinAccount/selectOne")
    UserAccountEntity selectOne(@RequestBody Wrapper wrapper);

    @PostMapping("/api/trade/withinAccount/update")
    boolean update(@RequestBody UserAccountEntity userAccountEntity,@RequestBody Wrapper wrapper);
}
