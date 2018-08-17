package cn.bitflash.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bitflash.trade.UserTradeJoinBuyEntity;
import cn.bitflash.utils.Common;
import cn.bitflash.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.bitflash.dao.UserTradeDao;
import cn.bitflash.trade.UserTradeBean;
import cn.bitflash.trade.UserTradeEntity;
import cn.bitflash.service.UserTradeService;

/**
 * @author wangjun
 * @date 2018年6月19日 下午4:48:48
 */

@Service("userTradeService" )
public class UserTradeServiceImpl extends ServiceImpl<UserTradeDao, UserTradeEntity> implements UserTradeService {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 计算出参考价格
     * 1.如果没有卖出数量则默认参考价格为0.325
     * 2.大于两条计算方式为总卖出数量除以总个数
     */
    @Override
    public Map<String, Object> selectTrade(Map<String, Object> param) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != param.get("uid" )) {
            try {
                BigDecimal big = new BigDecimal(0);
                BigDecimal divide = new BigDecimal(0);

                param.put("state", Common.STATE_SELL);
                List<UserTradeEntity> userTradeList = baseMapper.searchTrade(param);

                if (null != userTradeList && userTradeList.size() > 0) {
                    // 2.大于两条计算方式为总卖出数量除以总个数
                    for (int i = 0; i < userTradeList.size(); i++) {
                        UserTradeEntity userTradeEntity = userTradeList.get(i);
                        BigDecimal price = userTradeEntity.getPrice();
                        big = big.add(price);
                    }
                    Integer size = new Integer(userTradeList.size());
                    BigDecimal count = new BigDecimal(size);
                    divide = big.divide(count, 2, BigDecimal.ROUND_HALF_UP);
                    map.put("divide", divide);
                } else {
                    // 1.如果没有卖出数量则默认参考价格为0.33
                    map.put("divide", Common.MIN_PRICE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public Integer selectTradeCount(Map<String, Object> param) {
        Integer count = baseMapper.selectTradeCount(param);
        return count;
    }

    @Override
    public List<UserTradeEntity> queryTrade(Map<String, Object> param) {
        List<UserTradeEntity> list = baseMapper.selectTrade(param);
        return list;
    }

    public void updateTrade(Map<String, Object> param) {
        baseMapper.updateTrade(param);
    }

    public List<Map<String, Object>> selectTradeUrl(Map<String, Object> param) {
        List<Map<String, Object>> list = baseMapper.selectTradeUrl(param);
        return list;
    }

    /**
     * 查看订单明细
     *
     * @param param
     * @return
     */
    public UserTradeBean queryDetail(Map<String, Object> param) {
        UserTradeBean userTradeBean = baseMapper.queryDetail(param);
        return userTradeBean;
    }

    /**
     * 添加交易记录
     */
    public Integer insertUserTrade(UserTradeEntity userTradeEntity) {
        Integer i = baseMapper.insertUserTrade(userTradeEntity);
        return i;
    }

    public List<UserTradeEntity> searchTrade(Map<String, Object> param) {
        List<UserTradeEntity> list = baseMapper.searchTrade(param);
        return list;
    }

    @Override
    public List<UserTradeBean> selectTradeHistory(Map<String, Object> param) {
        List<UserTradeBean> list = baseMapper.selectTradeHistory(param);
        return list;
    }

    @Override
    public List<Map<String, Object>> getHistoryBystate5() {
        return baseMapper.getHistoryBystate5();
    }

    @Override
    public UserTradeEntity selectById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public UserTradeBean buyMessage(String id) {

        UserTradeBean userTradeBean = baseMapper.buyMessage(id);
        return userTradeBean;
    }

    @Override
    public List<UserTradeEntity> getByState(String state) {
        return baseMapper.getBystate(state);
    }

    //查询已完成订单
    public List<UserTradeJoinBuyEntity> selectFinishOrder(Map<String,Object> map) {
        return baseMapper.selectFinishOrder(map);
    }

}
