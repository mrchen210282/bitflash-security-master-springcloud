package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.service.AppStatusService;
import cn.bitflash.service.PlatFormConfigService;
import cn.bitflash.service.PriceLinechartService;
import cn.bitflash.system.AppStatusEntity;
import cn.bitflash.system.PriceLinechartEntity;
import cn.bitflash.utils.Common;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.utils.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author soso
 * @date 2018年5月22日 下午3:12:11
 */
@RestController
@RequestMapping("/api" )
public class ApiSystemController {

    @Autowired
    private AppStatusService appStatusService;

    @Autowired
    private PriceLinechartService priceLinechartService;

    @Autowired
    private PlatFormConfigService platFormConfigService;

    @GetMapping("update" )
    public R update(@RequestParam String appid, @RequestParam String version, @RequestParam String imei) {
        System.out.println(appid + "**" + version + "**" + imei);
        Map<String, String> map = new HashMap<String, String>();
        AppStatusEntity appStatusEntity = appStatusService.selectById(appid);
        if (appStatusEntity == null) {
            map.put("status", "-1" );
            return R.ok().put("data", map);
        }
        int old_v = Integer.valueOf(version.replace(".", "" )).intValue();
        int new_v = Integer.valueOf(appStatusEntity.getVersion().replace(".", "" )).intValue();
        if (new_v > old_v) {
            map.put("status", "1" );
            map.put("url", appStatusEntity.getUrl());
            map.put("note", appStatusEntity.getNote());
            map.put("title", appStatusEntity.getTitle());
        } else {
            map.put("status", "0" );
        }
        return R.ok().put("data", map);
    }

    /**
     * 获取当天起，前7天的价格
     * @return
     */
    @Login
    @PostMapping("getWeekPriceRate")
    public R getWeekPriceRate(){
        String str=platFormConfigService.getVal(Common.SHOW_DATE);
        Long time = Long.valueOf(str);
        Date now = new DateTime().withTimeAtStartOfDay().toDate();
        Date after = DateUtils.addDateDays(now,-7);
        DateTimeFormatter dt=DateTimeFormatter.ofPattern("MM-dd");
        List<PriceLinechartEntity> list=priceLinechartService.selectList(new EntityWrapper<PriceLinechartEntity>().between("rate_time",after,now).orderBy("rate_time"));
        List<String> date=list.stream().map(u->u.getRateTime().format(dt)).collect(Collectors.toList());
        List<Float> price=list.stream().map(PriceLinechartEntity::getPrice).collect(Collectors.toList());
        Date yesterday = DateUtils.addDateDays(now,-1);
        PriceLinechartEntity yestPrice = priceLinechartService.selectById(yesterday);
        return R.ok().put("date",date).put("price",price).put("yesterday",yestPrice);
    }

    @Login
    @PostMapping("getTime")
    public R getTime(){
        return R.ok(String.valueOf(System.currentTimeMillis()));
    }

}
