package cn.bitflash.FeignController;

import cn.bitflash.login.UserEntity;
import cn.bitflash.user.UserInfoEntity;
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

    @PostMapping("/api/user/selectById")
    public UserInfoEntity selectById(@RequestParam("uid") String uid);

    /**
     * 根据uid修改密码
     * @param userEntity
     * @return
     */
    @PostMapping("/api/user/update")
    public boolean update(UserEntity userEntity);
}
