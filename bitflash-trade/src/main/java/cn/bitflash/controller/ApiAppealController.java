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
import java.util.Date;
import java.util.List;

/**
 * @author gao
 */
@RestController
@RequestMapping("/api" )
public class ApiAppealController {

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
    public R userSend(@RequestParam String quantity, @RequestParam String uuid, @RequestParam String user_pwd , @LoginUser UserEntity user) {

}