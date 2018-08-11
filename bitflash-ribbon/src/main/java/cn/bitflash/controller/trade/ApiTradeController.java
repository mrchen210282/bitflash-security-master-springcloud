package cn.bitflash.controller.trade;

import cn.bitflash.login.RegisterForm;
import cn.bitflash.utils.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/trade")
public class ApiTradeController {

    @PostMapping("register")
    public R register(@RequestBody RegisterForm form, HttpServletResponse response) {
        return R.ok();
    }

    @PostMapping("selectTradeHistoryIncome")
    public Map selectTradeHistoryIncome(Map<String,Object> map) {

    }
}
