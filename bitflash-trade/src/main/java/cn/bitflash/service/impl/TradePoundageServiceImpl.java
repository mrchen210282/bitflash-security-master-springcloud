package cn.bitflash.service.impl;

import cn.bitflash.dao.TradePoundageDao;
import cn.bitflash.dao.UserBuyDao;
import cn.bitflash.service.TradePoundageService;
import cn.bitflash.service.UserBuyService;
import cn.bitflash.trade.TradePoundageEntity;
import cn.bitflash.trade.UserBuyEntity;
import cn.bitflash.trade.UserBuyMessageBean;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("tradePoundageService" )
public class TradePoundageServiceImpl extends ServiceImpl<TradePoundageDao, TradePoundageEntity> implements TradePoundageService {


}
