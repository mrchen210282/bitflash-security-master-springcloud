package cn.bitflash.controller;

import cn.bitflash.feignInterface.TradeHistoryRibbon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/tradeHistory")
public class TradeRibbonHistoryController {

    @Autowired
    private TradeHistoryRibbon tradeHistoryRibbon;

    @PostMapping("selectTradeHistoryIncome")
    public Map<String, Object> selectTradeHistoryIncome(Map<String, Object> map) {
        return tradeHistoryRibbon.selectTradeHistoryIncome(map);
    }
}
