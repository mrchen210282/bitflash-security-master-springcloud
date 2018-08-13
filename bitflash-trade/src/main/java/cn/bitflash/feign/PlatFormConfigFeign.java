package cn.bitflash.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(value = "bitflash-sys")
public interface PlatFormConfigFeign {

    @PostMapping("")
    Map<String,Object> getVal(String common);

}
