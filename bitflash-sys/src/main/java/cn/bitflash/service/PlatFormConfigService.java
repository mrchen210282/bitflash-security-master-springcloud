package cn.bitflash.service;

import cn.bitflash.system.PlatformConfigEntity;
import com.baomidou.mybatisplus.service.IService;

public interface PlatFormConfigService extends IService<PlatformConfigEntity> {

    public String getVal(String key);

}
