package cn.bitflash.feign;

import cn.bitflash.trade.UserAccountBean;
import cn.bitflash.trade.UserAccountEntity;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@FeignClient(value = "bitflash-trade")
public interface TradeFeign {

    @PostMapping("")
    Map<String,Object> selectTradeHistoryIncome(Map<String,Object> param);

    @PostMapping("")
    UserAccountBean selectUserAccount(Map<String,Object> param);

    @PostMapping("")
    UserAccountEntity selectOne(Wrapper<UserAccountEntity> param);

    @PostMapping("")
    UserAccountEntity selectById(String uid);

    @PostMapping("")
    List<Map<String, Object>> selectTradeUrl(Map<String,Object> param);

    @PostMapping("")
    boolean updateById(UserAccountEntity userAccountEntity);


}
