package cn.bitflash.service;

import cn.bitflash.trade.UserPayUrlEntity;
import cn.bitflash.trade.UserPayUrlEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author wangjun
 * @date 2018年6月19日 下午4:47:36
 */
public interface UserPayUrlService extends IService<UserPayUrlEntity> {

    public List<UserPayUrlEntity> selectUserPayUrlByParam(Map<String, Object> map);

    public void updateUserPayUrl(Map<String, Object> map);
}
