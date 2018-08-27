package cn.bitflash.dao;

import java.util.List;
import java.util.Map;

import cn.bitflash.trade.UserTradeEntity;
import cn.bitflash.trade.UserTradeJoinBuyEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.bitflash.trade.UserTradeBean;
import org.apache.ibatis.annotations.Param;

/**
 * @author wangjun
 * @date 2018年6月19日 下午4:45:51
 */
public interface UserTradeDao extends BaseMapper<UserTradeEntity> {

    //交易列表(卖入)
    public List<UserTradeBean> tradeList(Map<String, Object> param);

    public Integer tradeListCount(Map<String, Object> param);

    public List<UserTradeEntity> selectTrade(Map<String, Object> param);

    public Integer selectTradeCount(Map<String, Object> param);

    //订单列表(卖入)
    public List<UserTradeBean> selectOrderTrade(Map<String, Object> param);

    public Integer selectOrderTradeCount(Map<String, Object> param);

    public void updateTrade(Map<String, Object> param);

    public List<Map<String, Object>> selectTradeUrl(Map<String, Object> param);

    /**
     * 查看订单明细
     *
     * @param param
     * @return
     */
    public UserTradeBean queryDetail(Map<String, Object> param);

    public Integer insertUserTrade(UserTradeEntity userTradeEntity);

    public List<UserTradeEntity> searchTrade(Map<String, Object> param);

    /**
     * 查看交易历史
     *
     * @param param
     * @return
     */
    public List<UserTradeBean> selectTradeHistory(Map<String, Object> param);

    public List<Map<String, Object>> getHistoryBystate5();

    public UserTradeEntity selectById(String id);

    public UserTradeBean buyMessage(@Param("id") String id);

    List<UserTradeEntity> getBystate(@Param("state") String id);

    //查询已完成订单
    List<UserTradeJoinBuyEntity> selectFinishOrder(Map<String,Object> map);

    //查询已完成订单
    Integer selectFinishOrderCount(Map<String,Object> map);
}
