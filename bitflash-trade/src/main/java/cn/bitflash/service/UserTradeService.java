package cn.bitflash.service;

import java.util.List;
import java.util.Map;

import cn.bitflash.trade.UserTradeEntity;
import com.baomidou.mybatisplus.service.IService;

import cn.bitflash.trade.UserTradeBean;

/**
 * @author wangjun
 * @date 2018年6月19日 下午4:47:36
 */
public interface UserTradeService extends IService<UserTradeEntity> {

    public Map<String, Object> selectTrade(Map<String, Object> param);

    public Integer selectTradeCount(Map<String, Object> param);

    public List<UserTradeEntity> searchTrade(Map<String, Object> param);

    public List<UserTradeEntity> queryTrade(Map<String, Object> param);

    public void updateTrade(Map<String, Object> param);

    public List<Map<String, Object>> selectTradeUrl(Map<String, Object> param);

    /**
     * 查看订单明细
     *
     * @param param
     * @return
     */
    public UserTradeBean queryDetail(Map<String, Object> param);

    /**
     * 添加交易记录
     */
    public Integer insertUserTrade(UserTradeEntity userTradeEntity);

    /**
     * 查看交易历史
     *
     * @param param
     * @return
     */
    public List<UserTradeBean> selectTradeHistory(Map<String, Object> param);


    public List<Map<String, Object>> getHistoryBystate5();

    public UserTradeEntity selectById(String id);

    //获取买家信息
    public UserTradeBean buyMessage(String id);

    List<UserTradeEntity> getByState(String state);


}
