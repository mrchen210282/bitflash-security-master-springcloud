package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.annotation.UserAccount;
import cn.bitflash.feignInterface.UserFeign;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.UserBuyService;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.trade.UserBuyEntity;
import cn.bitflash.trade.UserBuyMessageBean;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 求购
 * @author chenchengyi
 *
 * 求购状态：
 *      求购者：‘1’：可撤销； ‘2’：待收款； ‘4’：待确认   ‘6’完成
 *      卖出者：    -     ； ‘3’：待付款； ‘5’：待收币   ‘6’完成
 */
@RestController
@RequestMapping("/api/need" )
public class ApiWanToBuyController {

    @Autowired
    private UserBuyService userBuyService;

    @Autowired
    private UserFeign userFeign;


    /**
     * 显示求购信息
     * @param user
     * @param pages
     * @param userAccount
     * @return
     */
    @Login
    @PostMapping("showBuyMessage" )
    public R showNeedMessage(@LoginUser UserEntity user, @RequestParam String pages, @UserAccount UserAccountEntity userAccount) {
        List<UserBuyMessageBean> ub = userBuyService.getBuyMessage(user.getUid(), Integer.valueOf(pages));
        if (ub.size() < 0 || ub == null) {
            return R.error("暂时没有求购信息" );
        }
        Integer count = userBuyService.getNumToPaging();
        return R.ok().put("count", count).put("list", ub).put("availableAssets", userAccount.getAvailableAssets());

    }

    /**
     * 添加求购信息
     * @param userBuyEntity
     * @param user
     * @return
     */
    @Login
    @PostMapping("addBuyMessage" )
    public R addBuyMessage(@RequestBody UserBuyEntity userBuyEntity, @LoginUser UserEntity user) {
        if (userBuyEntity == null) {
            return R.error(501, "求购信息为空" );
        }
        Double num = userBuyEntity.getQuantity();
        if (num % 100 != 0 || num < 100) {
            return R.error(502, "求购数量最低为100，且为100的倍数" );
        }
        userBuyService.addBuyMessage(userBuyEntity, user.getUid());
        return R.ok();
    }

    /**
     * 撤销求购信息
     * @param id
     * @param user
     * @return
     */
    @Login
    @PostMapping("cancel" )
    public R cancelBuyMessage(@RequestParam String id, @LoginUser UserEntity user) {
        UserBuyEntity ub = userBuyService.selectById(id);
        if (ub == null) {
            return R.error(501, "信息不存在" );
        }
        if (ub.getState().equals("0" )) {
            return R.error(502, "信息已经撤销" );
        }
        ub.setState("0" );
        ub.setCancel_time(new Date());
        userBuyService.updateById(ub);
        return R.ok();
    }

    /**
     * 显示求购者详情
     * @param id
     * @return
     */
    @Login
    @PostMapping("showDetailed" )
    public R showDetailed(@RequestParam String id) {
        UserBuyEntity ub = userBuyService.selectById(id);
        if (ub.getUid() == null) {
            return R.error(501, "订单不存在" );
        }
        UserInfoEntity ui = userFeign.selectUserInfoById(ub.getUid());



        if (ui == null) {
            return R.error(502, "卖出者信息不存在" );
        }
        return R.ok().put("buy", ub).put("user", ui);
    }


}
