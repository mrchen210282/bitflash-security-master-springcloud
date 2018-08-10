package cn.bitflash.controller;

import cn.bitflash.service.UserTradeHistoryService;
import cn.bitflash.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wangjun
 */
@RestController
@RequestMapping("/api" )
public class ApiTradeHistoryController {
    @Autowired
    private UserTradeHistoryService userTradeHistoryService;


    @PostMapping("/selectTradeHistoryIncome")
    public Map<String, Object> selectTradeHistoryIncome(@RequestParam Map<String, Object> params){
        Map<String, Object> map = userTradeHistoryService.selectTradeHistoryIncome(params);
        return map;
    }
}
