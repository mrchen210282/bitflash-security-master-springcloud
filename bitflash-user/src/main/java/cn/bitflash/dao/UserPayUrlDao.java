package cn.bitflash.dao;

import cn.bitflash.user.UserPayUrlEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author wangjun
 * @date 2018年6月19日 下午4:45:51
 */
public interface UserPayUrlDao extends BaseMapper<UserPayUrlEntity> {

    public List<UserPayUrlEntity> selectUserPayUrlByParam(Map<String, Object> map);

    public void updateUserPayUrl(Map<String, Object> map);
}
