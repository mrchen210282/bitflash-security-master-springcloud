package cn.bitflash.dao;

import cn.bitflash.trade.TradePoundageEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.Map;

public interface TradePoundageDao extends BaseMapper<TradePoundageEntity> {

    public void deleteTradePoundageById(Map<String,Object> map);
}
