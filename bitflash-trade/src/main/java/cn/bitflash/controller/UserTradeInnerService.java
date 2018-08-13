package cn.bitflash.controller;

import cn.bitflash.annotation.UserAccount;
import cn.bitflash.service.UserAccountService;
import cn.bitflash.trade.UserAccountBean;
import cn.bitflash.trade.UserAccountEntity;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequestMapping("/api/inner/trade")
public class UserTradeInnerService {

    @Autowired
    UserTradeInnerService userTradeInnerService;

    @Autowired
    UserAccountService userAccountService;

    @PostMapping("/selectTradeHistoryIncome")
    public Map<String,Object> selectTradeHistoryIncome(Map<String,Object> map){
        Map<String, Object> returnMap =  userTradeInnerService.selectTradeHistoryIncome(map);
        return returnMap;
    }

    @PostMapping("/selectUserAccount")
    public UserAccountBean selectUserAccount(Map<String, Object> map) {
        return userAccountService.selectUserAccount(map);
    }


    @PostMapping("/selectOne")
    public UserAccountEntity selectOne(@RequestBody Map<String,Object> map) {

        return null;
        //return this.selectOne(map).get;
    }

    @PostMapping("/selectById")
    public UserAccountEntity selectById(String uid) {
        return userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid",uid));
    }

}
