package cn.bitflash.controller;

import cn.bitflash.login.UserGTCidEntity;
import cn.bitflash.service.*;
import cn.bitflash.trade.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trade/inner")
public class UserTradeInnerService {
    @Autowired
    private UserTradeService userTradeService;

    @Autowired
    private UserTradeHistoryService userTradeHistoryService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserBuyHistoryService userBuyHistoryService;

    @PostMapping("/selectTradeHistoryIncome")
    public Map<String, Object> selectTradeHistoryIncome(@RequestBody Map<String, Object> map) {
        Map<String, Object> returnMap = userTradeHistoryService.selectTradeHistoryIncome(map);
        return returnMap;
    }

    @PostMapping("/selectUserAccount")
    public UserAccountBean selectUserAccount(@RequestBody Map<String, Object> map) {
        return userAccountService.selectUserAccount(map);
    }

    @PostMapping("/selectOne")
    public UserAccountEntity selectOne(@RequestBody Map<String, Object> map) {
        List<UserAccountEntity> list = userAccountService.selectByMap(map);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @PostMapping("/selectById")
    public UserTradeEntity selectById(@RequestParam("uid") String uid) {
        UserTradeEntity userTradeEntity = userTradeService.selectById(uid);
        return userTradeEntity;
    }

    @PostMapping("/insert")
    public boolean insert(@RequestBody UserAccountEntity userAccountEntity) {
        return userAccountService.insert(userAccountEntity);
    }

    @PostMapping("/selectOneTrade")
    public UserTradeEntity selectOneTrade(@RequestBody Map<String ,Object> map) {
        List<UserTradeEntity> trades = userTradeService.selectByMap(map);
        if(trades !=null && trades.size()>0){
            return trades.get(0);
        }
        return null;
    }

    @PostMapping("selectOneBuy")
    public UserBuyHistoryEntity selectOneBuy(@RequestBody Map<String ,Object> map){
        List<UserBuyHistoryEntity> buys = userBuyHistoryService.selectByMap(map);
        if(buys !=null && buys.size()>0){
            return buys.get(0);
        }
        return null;
    }

}
