package cn.bitflash.service.impl;

import cn.bitflash.dao.UserBuyDao;
import cn.bitflash.service.UserBuyService;
import cn.bitflash.trade.UserBuyBean;
import cn.bitflash.trade.UserBuyEntity;
import cn.bitflash.trade.UserBuyMessageBean;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("userBuyService" )
public class UserBuyServiceImpl extends ServiceImpl<UserBuyDao, UserBuyEntity> implements UserBuyService {


    @Override
    public List<UserBuyMessageBean> getBuyMessage(String uid, Integer pages) {
        return baseMapper.getBuyMessage(uid, pages);
    }

    @Override
    public Integer getNumToPaging() {
        return baseMapper.getNumToPaging();
    }

    @Override
    public void addBuyMessage(UserBuyEntity userBuyEntity, String uid) {
        userBuyEntity.setUid(uid);
        userBuyEntity.setCreateTime(new Date());
        userBuyEntity.setState("1" );
        baseMapper.insert(userBuyEntity);

    }

    @Override
    public List<UserBuyBean> selectBuyList(String uid){
        return baseMapper.selectBuyList(uid);
    }
}
