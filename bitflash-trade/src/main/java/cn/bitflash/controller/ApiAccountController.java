package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.UserAccount;
import cn.bitflash.service.UserBuyHistoryService;
import cn.bitflash.service.UserTradeHistoryService;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.trade.UserBuyHistoryEntity;
import cn.bitflash.trade.UserTradeHistoryEntity;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.usertradeutil.UserUtils;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
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

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private UserBuyHistoryService userBuyHistoryService;

    /**
     * 1.为判断成功 -1 为判断失败
     *
     * @author chen
     */
    @Login
    @PostMapping("/accountInfo")
    public R accountInfo(@UserAccount UserAccountEntity account) {
        String uid = account.getUid();
        UserInfoEntity infoEntity = userUtils.selectUserInfoById(uid);
        if (!infoEntity.getIsVip().equals("0")) {
            /**
             * yesterDayIncome 昨日收入
             * totelAssets   总收入
             * avaliableAssets 可用资产
             * frozenAssets 冻结资产
             */
            return R.ok().put("yesterDayIncome", account.getDailyIncome())
                    .put("totelAssets", account.getTotelIncome())
                    .put("avaliableAssets", account.getAvailableAssets())
                    .put("frozenAssets", account.getFrozenAssets())
                    .put("vip",infoEntity.getIsVip());
        }else{
            Date yesterday = DateUtils.addDateDays(new Date(), -1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String yester = sdf.format(yesterday);
            //交易
            List<UserTradeHistoryEntity> trades = userTradeHistoryService.selectList(new EntityWrapper<UserTradeHistoryEntity>().eq("purchase_uid",uid));
            Double alltrade = trades.stream().mapToDouble(u -> {
                Double money = u.getPurchaseQuantity().doubleValue();
                return money;
            }).sum();
            //昨日交易购买
            Double yesttrade = trades.stream().filter(u->sdf.format(u.getFinishTime()).equals(yester)).mapToDouble(u -> {
                Double money = u.getSellQuantity().doubleValue();
                return money;
            }).sum();

            //求购
            List<UserBuyHistoryEntity> buys = userBuyHistoryService. selectList(new EntityWrapper<UserBuyHistoryEntity>().eq("purchase_uid",uid));
            Double allbuy = buys.stream().mapToDouble(u -> {
                Double money = u.getQuantity().doubleValue();
                return money;
            }).sum();
            //昨日求购购买
            Double yestbuy = buys.stream().filter(u->sdf.format(u.getFinishTime()).equals(yester)).mapToDouble(u -> {
                Double money = u.getQuantity().doubleValue();
                return money;
            }).sum();
            /**
             * allbuy 总购买
             * yesterdaybuy 昨日购买
             * avaliableAssets 可用资产
             */
            return R.ok().put("allbuy",alltrade+allbuy)
                    .put("yesterdaybuy",yesttrade+yestbuy)
                    .put("avaliableAssets", account.getAvailableAssets())
                    .put("vip",infoEntity.getIsVip());
        }
    }
}
