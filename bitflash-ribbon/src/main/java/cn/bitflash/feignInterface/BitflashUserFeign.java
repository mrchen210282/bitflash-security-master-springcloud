package cn.bitflash.feignInterface;


import cn.bitflash.feignInterface.impl.UserFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "birflash-user",fallbackFactory = UserFeignFallback.class)
public interface BitflashUserFeign {
}
