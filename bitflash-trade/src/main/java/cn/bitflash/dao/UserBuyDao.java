package cn.bitflash.dao;

import cn.bitflash.trade.UserBuyBean;
import cn.bitflash.trade.UserBuyEntity;
import cn.bitflash.trade.UserBuyMessageBean;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserBuyDao extends BaseMapper<UserBuyEntity> {

    //获取求购信息
    List<UserBuyMessageBean> getBuyMessage(@Param("uid") String uid, @Param("pages") Integer pages);

    //获取求购信息数量
    Integer getNumToPaging();

    List<UserBuyBean> selectBuyList(String uid);

    List<UserBuyBean> selectAppealList(@Param("uid") String uid, @Param("pages") Integer pages);

}
