package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.annotation.UserAccount;
import cn.bitflash.common.utils.R;
import cn.bitflash.entity.*;
import cn.bitflash.service.UserBuyService;
import cn.bitflash.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author chenchengyi
 *
 */
@RestController
@RequestMapping("/api/need" )
public class ApiWanToBuyController {

    @Autowired
    private UserBuyService userBuyService;

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("showBuyMessage" )
    public R showNeedMessage(@LoginUser UserEntity user, @RequestParam String pages, @UserAccount UserAccountEntity userAccount) {
        List<UserBuyMessageBean> ub = userBuyService.getBuyMessage(user.getUid(), Integer.valueOf(pages));
        if (ub.size() < 0 || ub == null) {
            return R.error("暂时没有求购信息" );
        }
        Integer count = userBuyService.getNumToPaging();
        return R.ok().put("count", count).put("list", ub).put("availableAssets", userAccount.getAvailableAssets());

    }

    @Login
    @PostMapping("addBuyMessage" )
    @ApiOperation("添加求购信息" )
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

    @Login
    @PostMapping("cancel" )
    @ApiOperation("撤销求购信息" )
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

    @Login
    @PostMapping("showDetailed" )
    @ApiOperation("显示求购者详情" )
    public R showDetailed(@RequestParam String id) {
        UserBuyEntity ub = userBuyService.selectById(id);
        if (ub.getUid() == null) {
            return R.error(501, "订单不存在" );
        }
        UserInfoEntity ui = userInfoService.selectById(ub.getUid());
        if (ui == null) {
            return R.error(502, "卖出者信息不存在" );
        }
        return R.ok().put("buy", ub).put("user", ui);
    }


}
