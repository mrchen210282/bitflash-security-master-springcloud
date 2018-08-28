package cn.bitflash.service;

import java.util.List;

import cn.bitflash.trade.UserBuyBean;
import com.baomidou.mybatisplus.service.IService;
import cn.bitflash.trade.UserBuyEntity;
import cn.bitflash.trade.UserBuyMessageBean;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 求购
 *
 * @author gaoyuguo
 * @date 2018-8-28 15:22:06
 */
public interface UserBuyService extends IService<UserBuyEntity> {

    /**
     * @param uid   用户id
     * @param pages 分页
     * @return 除用户所有求购信息
     */
    List<UserBuyMessageBean> getBuyMessage(String uid, Integer pages);

    /**
     * @return 除用户所有求购信息数量
     */
    Integer getNumToPaging();

    /**
     * 添加求购信息
     *
     * @param userBuyEntity 订单详情
     * @param uid           用户id
     */
    void addBuyMessage(UserBuyEntity userBuyEntity, String uid);

    /**
     * @param uid   用户id
     * @param pages 分页
     * @return 用户所有求购信息
     */
    List<UserBuyBean> selectBuyList(String uid, Integer pages);

    /**
     * @param uid 用户id
     * @return 用户所有求购信息数量
     */
    Integer selectUserBuyOwnCount(String uid);

    /**
     * @param uid   用户id
     * @param pages 分页
     * @return 用户申诉记录
     */
    List<UserBuyBean> selectAppealList(@RequestParam("uid") String uid, @RequestParam("pages") Integer pages);

    /**
     * @param uid 用户id
     * @return 用户申诉记录数量
     */
    Integer selectAppealCount(@RequestParam("uid") String uid);

}
