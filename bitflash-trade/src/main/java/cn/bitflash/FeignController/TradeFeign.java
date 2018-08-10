package cn.bitflash.FeignController;

import cn.bitflash.user.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "bitflash-user")
public interface TradeFeign {

    @PostMapping("/api/user/selectOne")
    public List<UserEntity> selectOne(@RequestParam("params") Map<String, Object> params);
}
