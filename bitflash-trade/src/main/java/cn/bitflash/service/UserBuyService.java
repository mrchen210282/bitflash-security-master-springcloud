package cn.bitflash.service;

import java.util.List;

import cn.bitflash.trade.UserBuyBean;
import com.baomidou.mybatisplus.service.IService;
import cn.bitflash.trade.UserBuyEntity;
import cn.bitflash.trade.UserBuyMessageBean;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserBuyService extends IService<UserBuyEntity> {

    //获取求购信息
    List<UserBuyMessageBean> getBuyMessage(String uid, Integer pages);

    //获取求购信息数量
    Integer getNumToPaging();

    void addBuyMessage(UserBuyEntity userBuyEntity, String uid);

    List<UserBuyBean> selectBuyList(String uid);

    List<UserBuyBean> selectAppealList(@RequestParam("uid") String uid, @RequestParam("pages") Integer pages);

    Integer selectAppealCount(@RequestParam("uid")String uid);

    Integer selectUserBuyOwnCount(String uid);
}
