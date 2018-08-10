package cn.bitflash.service.impl;

import java.util.List;
import java.util.Map;

import cn.bitflash.trade.UserPayUrlEntity;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.bitflash.dao.UserPayUrlDao;
import cn.bitflash.trade.UserPayUrlEntity;
import cn.bitflash.service.UserPayUrlService;

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
