package cn.bitflash.service;

import cn.bitflash.user.PlatformConfigEntity;
import com.baomidou.mybatisplus.service.IService;

public interface PlatFormConfigService extends IService<PlatformConfigEntity> {

    public String getVal(String key);

}
