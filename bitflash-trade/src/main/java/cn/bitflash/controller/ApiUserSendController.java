package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.feignInterface.LoginFeign;
import cn.bitflash.feignInterface.UserFeign;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.UserAccountService;
import cn.bitflash.service.UserBrokerageService;
import cn.bitflash.service.UserSendService;
import cn.bitflash.service.UserTradeConfigService;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.trade.UserBrokerageEntity;
import cn.bitflash.trade.UserSendEntity;

import cn.bitflash.trade.UserTradeConfigEntity;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author gao
 */
@RestController
@RequestMapping("/api")
public class ApiUserSendController {

    @Autowired
    private UserSendService userSendService;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private LoginFeign loginFeign;

    @Autowired
    private UserBrokerageService userBrokerageService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserTradeConfigService userTradeConfigService;

    /**
     * @param quantity 发送
     * @author 1.查询赠送对象是否存在，若不存在返回code=1错误
     * 2.向user_account表中，扣除发送用户的余额，并向接收者添加余额
     * 3.向user_send表中添加赠送记录
     * 4.向user_brokeage表中添加手续费记录
     */
    @Login
    @PostMapping("userSend")
    public R userSend(@RequestParam String quantity, @RequestParam String uuid, @RequestParam String user_pwd, @LoginUser UserEntity user) {

        //交易状态：‘-1’余额不足错误；‘0’操作成功；‘1’用户不存在；‘2’其他错误；‘3’交易数量错误；‘4’交易密码错误
        int code = 2;
        //业务状态
        boolean relation = false;

        //如果用户不存在,返回‘用户不存在’错误。
        UserEntity Sendee = loginFeign.selectOneByUser(new ModelMap("uuid", uuid));
        if (Sendee == null || "".equals(Sendee)) {
            //用户不存在
            code = 1;
            return R.ok().put("code", code);
        }

        //如果交易密码不正确,返回错误
        UserPayPwdEntity pwd = userFeign.selectUserPayPwd(new ModelMap("uid", user.getUid()));
        if (!user_pwd.equals(pwd.getPayPassword())) {
            // 交易密码不正确
            code = 4;
            return R.ok().put("code", code);
        }

        //赠送数量
        //String 转换成 float
        DecimalFormat df = new DecimalFormat("#########.##");
        Float quantitys = Float.parseFloat(quantity);
        //赠送数量转换成BigDecimal
        BigDecimal user_quantity = new BigDecimal(quantitys);

        //手续费
        UserTradeConfigEntity userTradeConfig = userTradeConfigService.selectOne(new EntityWrapper<UserTradeConfigEntity>().eq("remark", "发送手续费"));
        Float poundage = userTradeConfig.getPoundage();

        UserBrokerageEntity userbroker = userBrokerageService.selectOne(new EntityWrapper<UserBrokerageEntity>().eq("id", 1));
        //2.5%手续费
        BigDecimal user_brokerage = new BigDecimal(quantitys * poundage);

        //Send在user——account中修改
        UserAccountEntity send_account = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", user.getUid()));
        //扣款数量
        BigDecimal user_quantitys = user_quantity.add(user_brokerage);
        //账号总额大于扣款
        if (user_quantitys.compareTo(send_account.getAvailableAssets()) == -1 || user_quantitys.compareTo(send_account.getAvailableAssets()) == 0) {

            //如果regulateRelease数量足够扣款
            if (user_quantitys.compareTo(send_account.getRegulateRelease()) == -1 || user_quantitys.compareTo(send_account.getRegulateRelease()) == 0) {
                BigDecimal regulateRelease = send_account.getRegulateRelease().subtract(user_quantitys);
                BigDecimal availableAssets = send_account.getAvailableAssets().subtract(user_quantitys);
                send_account.setRegulateRelease(regulateRelease);
                send_account.setAvailableAssets(availableAssets);
                //更新数据
                userAccountService.update(send_account, new EntityWrapper<UserAccountEntity>().eq("uid", user.getUid()));
                relation = true;
            } else {//如果regulateRelease数量不足够扣款
                //查询regulate_income
                BigDecimal surplus = user_quantitys.subtract(send_account.getRegulateRelease());
                BigDecimal regulate_income = send_account.getRegulateIncome().subtract(surplus);
                BigDecimal availableAssets = send_account.getAvailableAssets().subtract(user_quantitys);
                send_account.setRegulateRelease(new BigDecimal(0.00));
                send_account.setAvailableAssets(availableAssets);
                send_account.setRegulateIncome(regulate_income);
                //更新数据
                userAccountService.update(send_account, new EntityWrapper<UserAccountEntity>().eq("uid", user.getUid()));
                relation = true;
            }
            //Sendee在user——account中修改
            if (relation) {
                UserAccountEntity sendee_account = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", Sendee.getUid()));

                BigDecimal regulate_income = sendee_account.getRegulateIncome().add(user_quantity);
                BigDecimal availableAssets = sendee_account.getAvailableAssets().add(user_quantity);
                sendee_account.setRegulateIncome(regulate_income);
                sendee_account.setAvailableAssets(availableAssets);
                //更新数据
                userAccountService.update(sendee_account, new EntityWrapper<UserAccountEntity>().eq("uid", Sendee.getUid()));
            }

        } else {
            //数量不够扣款
            code = -1;
            //交易失败
            return R.ok().put("code", code);
        }

        //添加数据user_send,添加赠送记录
        if (relation) {
            UserSendEntity us = new UserSendEntity();
            us.setQuantity(quantitys);
            Date day = new Date();
            us.setSendTime(day);
            us.setSendUid(user.getUid());
            us.setSendeeUid(Sendee.getUid());
            userSendService.insert(us);
        }

        //如果双方交易完成
        if (relation) {
            BigDecimal get_brokerages = user_brokerage.add(userbroker.getSellBrokerage());
            userbroker.setSellBrokerage(get_brokerages);
            //修改到user_brokerage
            userBrokerageService.update(userbroker, new EntityWrapper<UserBrokerageEntity>().eq("id", 1));
            code = 0;
        }
        return R.ok().put("code", code);
    }

    /**
     * 交易记录
     *
     * @author
     */
    @Login
    @PostMapping("record")
    public R record(@LoginUser UserEntity user, @RequestParam int state) {

        //state = 1 :发送
        if (state == 1) {
            List<UserSendEntity> usersendList = userSendService.selectaccount(user.getUid());
            return R.ok().put("usersendList", usersendList);
        }
        //state = 2 :接收
        else if (state == 2) {
            List<UserSendEntity> useracceptList = userSendService.selectaccept(user.getUid());
            return R.ok().put("useracceptList", useracceptList);
        }
        return null;
    }

}