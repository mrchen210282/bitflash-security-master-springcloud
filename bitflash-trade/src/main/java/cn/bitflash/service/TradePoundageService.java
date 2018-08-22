package cn.bitflash.service;

import cn.bitflash.trade.TradePoundageEntity;
import cn.bitflash.trade.UserBuyEntity;
import cn.bitflash.trade.UserBuyMessageBean;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

public interface TradePoundageService extends IService<TradePoundageEntity> {

    public void deleteTradePoundageById(Map<String,Object> map);
}
