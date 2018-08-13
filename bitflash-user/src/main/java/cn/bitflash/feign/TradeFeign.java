package cn.bitflash.feign;

import cn.bitflash.feign.impl.TradeFallback;
import cn.bitflash.trade.UserAccountBean;
import cn.bitflash.trade.UserAccountEntity;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "bitflash-trade",fallbackFactory = TradeFallback.class)
public interface TradeFeign {

    @PostMapping("/api/trade/selectTradeHistoryIncome")
    Map<String,Object> selectTradeHistoryIncome(Map<String,Object> param);

    @PostMapping("")
    UserAccountBean selectUserAccount(Map<String,Object> param);

    @PostMapping("")
    UserAccountEntity selectOne(Wrapper<UserAccountEntity> param);

    @PostMapping("/api/trade/selectById")
    UserAccountEntity selectById(@RequestParam("uid") String uid);

    @PostMapping("")
    List<Map<String, Object>> selectTradeUrl(Map<String,Object> param);

    @PostMapping("")
    boolean updateById(UserAccountEntity userAccountEntity);


}
