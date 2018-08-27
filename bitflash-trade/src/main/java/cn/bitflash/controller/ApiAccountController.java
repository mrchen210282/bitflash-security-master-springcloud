package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.UserAccount;
import cn.bitflash.service.UserTradeHistoryService;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.trade.UserTradeHistoryEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.utils.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 获取账户信息
 *
 * @author eric
 */
@RestController
@RequestMapping("/api")
public class ApiAccountController {


    @Autowired
    private UserTradeHistoryService userTradeHistoryService;


    /**
     * 1.为判断成功 -1 为判断失败
     *
     * @author chen
     */
    @Login
    @PostMapping("/accountInfo")
    public R accountInfo(@UserAccount UserAccountEntity account) {
        String uid = account.getUid();
        Date now = new DateTime().withTimeAtStartOfDay().toDate();
        Date yesterday = DateUtils.addDateDays(now, -1);
        List<UserTradeHistoryEntity> list = userTradeHistoryService.selectList(new EntityWrapper<UserTradeHistoryEntity>()
                .eq("purchase_uid", uid).between("create_time", yesterday, now).eq("state", 4));
        Double yesterdayBkc = list.stream().mapToDouble(u -> {
            Double money = u.getSellQuantity().doubleValue();
            return money;
        }).sum();
        if(yesterdayBkc==null){
            yesterdayBkc=0.0d;
        }

        /**
         * yesterDayIncome 昨日收入
         * totelAssets   总收入
         * avaliableAssets 可用资产
         * frozenAssets 冻结资产
         */
        return R.ok().put("yesterDayIncome", yesterdayBkc)
                .put("totelAssets", account.getTotelAssets())
                .put("avaliableAssets", account.getAvailableAssets())
                .put("frozenAssets", account.getFrozenAssets());
    }
}
