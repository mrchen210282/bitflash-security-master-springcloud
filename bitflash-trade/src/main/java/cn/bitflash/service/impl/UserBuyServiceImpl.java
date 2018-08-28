package cn.bitflash.service.impl;

import cn.bitflash.dao.UserBuyDao;
import cn.bitflash.service.UserBuyService;
import cn.bitflash.trade.UserBuyBean;
import cn.bitflash.trade.UserBuyEntity;
import cn.bitflash.trade.UserBuyMessageBean;
import cn.bitflash.utils.Common;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static cn.bitflash.utils.Common.STATE_BUY_CANCEL;

@Service("userBuyService")
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
        userBuyEntity.setId(Common.randomUtil());
        System.out.println(userBuyEntity.getId());
        userBuyEntity.setUid(uid);
        userBuyEntity.setCreateTime(new Date());
        userBuyEntity.setState(STATE_BUY_CANCEL);
        baseMapper.insert(userBuyEntity);

    }

    @Override
    public List<UserBuyBean> selectBuyList(String uid, Integer pages) {
        return baseMapper.selectBuyList(uid, pages);
    }

    @Override
    public List<UserBuyBean> selectAppealList(String uid, Integer pages) {
        return baseMapper.selectAppealList(uid, pages);
    }

    @Override
    public Integer selectAppealCount(String uid) {
        return baseMapper.selectAppealCount(uid);
    }

    @Override
    public Integer selectUserBuyOwnCount(String uid) {
        return baseMapper.selectUserBuyOwnCount(uid);
    }
}
