package cn.bitflash.dao;

import cn.bitflash.user.PlatformConfigEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatFormConfigDao extends BaseMapper<PlatformConfigEntity> {

    public String getValue(@Param("key") String key);
}
