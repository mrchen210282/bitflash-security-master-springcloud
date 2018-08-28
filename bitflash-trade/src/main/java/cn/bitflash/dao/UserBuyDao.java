package cn.bitflash.dao;

import cn.bitflash.trade.UserBuyBean;
import cn.bitflash.trade.UserBuyEntity;
import cn.bitflash.trade.UserBuyMessageBean;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 求购
 *
 * @author gaoyuguo
 * @date 2018-8-28 15:22:06
 */
public interface UserBuyDao extends BaseMapper<UserBuyEntity> {

    List<UserBuyMessageBean> getBuyMessage(@Param("uid") String uid, @Param("pages") Integer pages);

    Integer getNumToPaging();

    List<UserBuyBean> selectBuyList(@Param("uid") String uid, @Param("pages") Integer pages);

    List<UserBuyBean> selectAppealList(@Param("uid") String uid, @Param("pages") Integer pages);

    Integer selectAppealCount(String uid);

    Integer selectUserBuyOwnCount(@Param("uid") String uid);
}
