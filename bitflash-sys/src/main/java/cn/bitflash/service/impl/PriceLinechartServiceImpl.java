package cn.bitflash.service.impl;

import cn.bitflash.dao.PriceLinechartDao;
import cn.bitflash.service.PriceLinechartService;
import cn.bitflash.system.PriceLinechartEntity;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("priceLinechartService")
public class PriceLinechartServiceImpl extends ServiceImpl<PriceLinechartDao,PriceLinechartEntity> implements PriceLinechartService {
}
