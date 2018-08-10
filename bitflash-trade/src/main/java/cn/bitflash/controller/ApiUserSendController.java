package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.UserAccountService;
import cn.bitflash.service.UserBrokerageService;
import cn.bitflash.service.UserSendService;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.trade.UserBrokerageEntity;
import cn.bitflash.trade.UserSendEntity;

import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * @author gao
 */
@RestController
@RequestMapping("/api" )
public class ApiUserSendController {

    @Autowired
    private UserSendService userSendService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserBrokerageService userBrokerageService;

    @Autowired
    private UserAccountService userAccountService;

    /**
     * @param quantity 发送
     * @author 1.查询赠送对象是否存在，若不存在返回code=1错误
     * 2.向user_account表中，扣除发送用户的余额，并向接收者添加余额
     * 3.向user_send表中添加赠送记录
     * 4.向user_brokeage表中添加手续费记录
     */
    @PostMapping("userSend" )
    public R userSend(@RequestParam String quantity, @RequestParam String uuid, @LoginUser UserEntity user) {

        //交易状态：
        //‘1’用户不存在；‘0’操作成功；‘-1’余额不足无法扣款
        int code = 3;
        //业务状态
        boolean relation = false;
        //获取Sendee_uid，如果用户不存在返回‘用户不存在’错误。
        UserEntity Sendee = userService.selectOne(new EntityWrapper<UserEntity>().eq("uuid", uuid));
        String Sendee_uid = "";
        if (Sendee != null) {
            Sendee_uid = Sendee.getUid();
        } else {
            //用户不存在
            code = 1;
            return R.ok().put("code", code);
        }


        //赠送数量
        //String 转换成 float
        DecimalFormat df = new DecimalFormat("#########.##" );
        Float quantitys = Float.parseFloat(quantity);
        //赠送数量转换成BigDecimal
        BigDecimal user_quantity = new BigDecimal(quantitys);
        if(user_quantity.equals(0)){
            return R.ok();
        }

        //手续费
        UserBrokerageEntity userbroker = userBrokerageService.selectOne(new EntityWrapper<UserBrokerageEntity>().eq("id", 1));
        //1%手续费
        BigDecimal user_brokerage = new BigDecimal(quantitys * 0.01f);


        //Send在user——account中修改
        UserAccountEntity sendAccount = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", user.getUid()));
        //扣款数量
        BigDecimal user_quantitys = user_quantity.add(user_brokerage);
        //账号总额大于扣款
        if (user_quantitys.compareTo(sendAccount.getAvailableAssets()) == -1 || user_quantitys.compareTo(sendAccount.getAvailableAssets()) == 0) {
            //如果regulateRelease数量足够扣款
            if (user_quantitys.compareTo(sendAccount.getRegulateRelease()) == -1 || user_quantitys.compareTo(sendAccount.getRegulateRelease()) == 0) {
                BigDecimal regulateRelease = sendAccount.getRegulateRelease().subtract(user_quantitys);
                BigDecimal availableAssets = sendAccount.getAvailableAssets().subtract(user_quantitys);
                sendAccount.setRegulateRelease(regulateRelease);
                sendAccount.setAvailableAssets(availableAssets);
                //更新数据
                userAccountService.update(sendAccount, new EntityWrapper<UserAccountEntity>().eq("uid", user.getUid()));
                relation = true;
            } else {
                //如果regulateRelease数量不足够扣款
                //查询regulate_income
                BigDecimal surplus = user_quantitys.subtract(sendAccount.getRegulateRelease());
                BigDecimal regulate_income = sendAccount.getRegulateIncome().subtract(surplus);
                BigDecimal availableAssets = sendAccount.getAvailableAssets().subtract(user_quantitys);
                sendAccount.setRegulateRelease(new BigDecimal(0.00));
                sendAccount.setAvailableAssets(availableAssets);
                sendAccount.setRegulateIncome(regulate_income);
                //更新数据
                userAccountService.update(sendAccount, new EntityWrapper<UserAccountEntity>().eq("uid", user.getUid()));
                relation = true;
            }
            //Sendee在user——account中修改
            if (relation) {
                UserAccountEntity sendee_account = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", Sendee_uid));
                BigDecimal regulate_income = sendee_account.getRegulateIncome().add(user_quantity);
                BigDecimal availableAssets = sendee_account.getAvailableAssets().add(user_quantity);
                sendee_account.setRegulateIncome(regulate_income);
                sendee_account.setAvailableAssets(availableAssets);
                //更新数据
                userAccountService.update(sendee_account, new EntityWrapper<UserAccountEntity>().eq("uid", Sendee_uid));
            }
        } else {
            //数量不够扣款
            code = -1;
            //交易失败
            relation = false;
        }

        //添加数据user_send,添加赠送记录
        if (relation) {
            UserSendEntity us = new UserSendEntity();
            us.setQuantity(quantitys);
            Date day = new Date();
            us.setSendTime(day);
            us.setSendUid(user.getUid());
            us.setSendeeUid(Sendee_uid);
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
    @PostMapping("record" )
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
