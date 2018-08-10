package cn.bitflash.service.impl;

import cn.bitflash.dao.UserPayUrlDao;
import cn.bitflash.service.UserPayUrlService;
import cn.bitflash.user.UserPayUrlEntity;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userPayUrlService" )
public class UserPayUrlServiceImpl extends ServiceImpl<UserPayUrlDao, UserPayUrlEntity> implements UserPayUrlService {

    public List<UserPayUrlEntity> selectUserPayUrlByParam(Map<String, Object> map) {
        List<UserPayUrlEntity> list = baseMapper.selectUserPayUrlByParam(map);
        return list;
    }

    public void updateUserPayUrl(Map<String, Object> map) {
        baseMapper.updateUserPayUrl(map);
    }

}
