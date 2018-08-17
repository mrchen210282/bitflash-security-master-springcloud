package cn.bitflash.service;

import cn.bitflash.trade.UserBuyBean;
import cn.bitflash.trade.UserBuyEntity;
import cn.bitflash.trade.UserBuyMessageBean;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

public interface UserBuyService extends IService<UserBuyEntity> {

    //获取求购信息
    List<UserBuyMessageBean> getBuyMessage(String uid, Integer pages);

    //获取求购信息数量
    Integer getNumToPaging();

    void addBuyMessage(UserBuyEntity userBuyEntity, String uid);

    List<UserBuyBean> selectBuyList(String uid);

    List<UserBuyBean> selectAppealList(String uid);
}
