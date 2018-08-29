package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.UserAccount;
import cn.bitflash.service.UserBuyService;
import cn.bitflash.service.UserComplaintService;
import cn.bitflash.service.UserTradeConfigService;
import cn.bitflash.service.UserTradeService;
import cn.bitflash.trade.*;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 申诉
 *
 * @author gaoyuguo
 * @date 2018-8-28 15:22:06
 */
@RestController
@RequestMapping("/api/appeal")
public class ApiComplaintController {

    @Autowired
    private UserBuyService userBuyService;

    @Autowired
    private UserComplaintService userComplaintService;

    @Autowired
    private UserTradeConfigService userTradeConfigService;

    @Autowired
    private UserTradeService userTradeService;

    /**
     * 申诉列表
     *
     * @param pages       分页
     * @param userAccount 账号信息
     * @return count信息数量   list申诉列表    availableAssets可用资产
     */
    @Login
    @PostMapping("/List")
    public R selectAppealList(@RequestAttribute("uid")String uid, @RequestParam("pages") String pages, @UserAccount UserAccountEntity userAccount) {
        List<UserBuyBean> ub = userBuyService.selectAppealList(uid, Integer.valueOf(pages));
        if (ub == null || ub.size() < 0) {
            return R.error("暂时没有申诉信息");
        }
        Integer count = userBuyService.selectAppealCount(uid);
        return R.ok().put("count", count).put("list", ub).put("availableAssets", userAccount.getAvailableAssets());
    }

    /**
     * 申诉详情
     *
     * @param id 订单id
     * @return userComplaintBean 订单详情
     */
    @PostMapping("/check")
    public R checkAppeal(@RequestParam("id") String id) {
        UserComplaintBean userComplaintBean = userComplaintService.getComplaintMessage(id);

        //判定订单不存在
        if (userComplaintBean == null) {
            return R.ok().put("code", "订单不存在");
        }

        Map<String, Float> map = this.poundage(id, userComplaintBean.getComplaintState());

        return R.ok().put("userComplaintBean", userComplaintBean).put("totalQuantity", map.get("totalQuantity")).put("price", map.get("price")).put("buyQuantity", map.get("buyQuantity")).put("totalMoney", map.get("totalMoney"));
    }

    /**
     * 手续费
     *
     * @param id    订单id
     * @param state 买入/卖出
     * @return 手续费详情
     */
    public Map<String, Float> poundage(String id, String state) {

        DecimalFormat df = new DecimalFormat("#########.##");
        Float buyQuantity = 0f;
        Float price = 0f;
        if (state.equals("0")) {
            UserTradeEntity userTradeEntity = userTradeService.selectById(Integer.parseInt(id));
            price = Float.parseFloat(df.format(userTradeEntity.getPrice()));
            buyQuantity = Float.parseFloat(df.format(userTradeEntity.getQuantity()));
        } else if (state.equals("1")) {
            UserBuyEntity userBuy = userBuyService.selectById(Integer.parseInt(id));
            price = Float.parseFloat(df.format(userBuy.getPrice()));
            buyQuantity = Float.parseFloat(df.format(userBuy.getQuantity()));
        }

        //手续费比率
        Float poundage = userTradeConfigService.selectOne(new EntityWrapper<UserTradeConfigEntity>().eq("remark", "交易手续费")).getPoundage();
        //Float poundage = Float.parseFloat(df.format(poundagePer));
        //手续费数量
        Float totalPoundage = buyQuantity * poundage;
        //实际交易总数量
        Float totalQuantity = buyQuantity + totalPoundage;
        //总价格
        Float totalMoney = buyQuantity * (price);

        Map<String, Float> map = new HashMap<String, Float>();
        map.put("buyQuantity", buyQuantity);
        map.put("poundage", poundage);
        map.put("totalPoundage", totalPoundage);
        map.put("totalQuantity", totalQuantity);
        map.put("price", price);
        map.put("totalMoney", totalMoney);
        return map;
    }

}