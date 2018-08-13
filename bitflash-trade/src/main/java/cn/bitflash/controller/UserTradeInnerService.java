package cn.bitflash.controller;

import cn.bitflash.login.UserGTCidEntity;
import cn.bitflash.service.UserAccountService;
import cn.bitflash.service.UserTradeHistoryService;
import cn.bitflash.service.UserTradeService;
import cn.bitflash.trade.UserAccountBean;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.trade.UserTradeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/trade/inner")
public class UserTradeInnerService {

    @Autowired
    UserTradeService userTradeService;

    @Autowired
    UserTradeHistoryService userTradeHistoryService;

    @Autowired
    UserAccountService userAccountService;


    @PostMapping("/selectTradeHistoryIncome")
    public Map<String, Object> selectTradeHistoryIncome(Map<String,Object> map){
        Map<String, Object> returnMap =  userTradeHistoryService.selectTradeHistoryIncome(map);
        return returnMap;
    }

    @PostMapping("/selectUserAccount")
    public UserAccountBean selectUserAccount(Map<String, Object> map) {
        return userAccountService.selectUserAccount(map);
    }

    @PostMapping("/selectOne")
    public UserAccountEntity selectOne(@RequestBody Map<String,Object> map) {
        List<UserAccountEntity> list = userAccountService.selectByMap(map);
        if(list.size()>0){
            return list.get(0);
        }
        return null;

    }


    @PostMapping("/wthinGT/selectOne")
    public UserGTCidEntity selectOneByGT(@RequestBody Map<String,Object> map){
        //return userGTCidService.selectByMap(map).get(0);
        return null;
    }

    @PostMapping("/selectById")
    public UserTradeEntity selectById(@RequestParam("uid") String uid){
        UserTradeEntity userTradeEntity = userTradeService.selectById(uid);
        return userTradeEntity;
    }

    @PostMapping("/insert")
    public boolean insert(@RequestBody UserAccountEntity userAccountEntity){
        return userAccountService.insert(userAccountEntity);
    }




}
