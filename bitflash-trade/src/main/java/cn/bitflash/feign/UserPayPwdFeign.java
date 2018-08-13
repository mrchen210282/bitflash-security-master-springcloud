package cn.bitflash.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "bitflash-userInfo")
public interface UserPayPwdFeign {

}
