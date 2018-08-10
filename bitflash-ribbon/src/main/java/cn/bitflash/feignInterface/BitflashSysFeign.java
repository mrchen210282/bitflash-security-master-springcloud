package cn.bitflash.feignInterface;

import cn.bitflash.feignInterface.impl.SysFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "bitflash-sys",fallbackFactory =SysFeignFallback.class)
public interface BitflashSysFeign {
}
