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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api")
public class ApiSystemController {

    private final Logger logger = LoggerFactory.getLogger(ApiSystemController.class);

    @Autowired
    private AppStatusService appStatusService;

    @Autowired
    private PriceLinechartService priceLinechartService;

    @Autowired
    private PlatFormConfigService platFormConfigService;

    @GetMapping("update")
    public R update(@RequestParam String appid, @RequestParam String version, @RequestParam String imei) {
        logger.info(appid + "**" + version + "**" + imei);
        Map<String, String> map = new HashMap<String, String>();
        AppStatusEntity appStatusEntity = appStatusService.selectById(appid);
        if (appStatusEntity == null) {
            map.put("status", "-1");
            return R.ok().put("data", map);
        }
        String[] newversion = appStatusEntity.getVersion().split("\\.");
        String[] oldversion = version.split("\\.");

        if (newversion.length - oldversion.length != 0) {
            map.put("status", "1");
            map.put("url", appStatusEntity.getUrl());
            map.put("note", appStatusEntity.getNote());
            map.put("title", appStatusEntity.getTitle());
            return R.ok().put("data", map);
        }
        for (int i = 0; i < newversion.length; i++) {
            if (!newversion[i].equals(oldversion[i])) {
                map.put("status", "1");
                map.put("url", appStatusEntity.getUrl());
                map.put("note", appStatusEntity.getNote());
                map.put("title", appStatusEntity.getTitle());
                return R.ok().put("data", map);
            } else {
                map.put("status", "0");

            }
        }
        return R.ok().put("data", map);
    }


    /**
     * 获取当天起，前7天的价格
     *
     * @return
     */
    @Login
    @GetMapping("getWeekPriceRate")
    public R getWeekPriceRate() {
        /*String str=platFormConfigService.getVal(Common.SHOW_DATE);
        Long time = Long.valueOf(str);*/
        Date now = new DateTime().withTimeAtStartOfDay().toDate();
        Date after = DateUtils.addDateDays(now, -7);
        Date yesterday = DateUtils.addDateDays(now, -1);
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("MM-dd");
        List<PriceLinechartEntity> list = priceLinechartService.selectList(new EntityWrapper<PriceLinechartEntity>().between("rate_time", after, yesterday).orderBy("rate_time"));
        List<String> date = list.stream().map(u -> u.getRateTime().format(dt)).collect(Collectors.toList());
        List<Float> price = list.stream().map(PriceLinechartEntity::getPrice).collect(Collectors.toList());
        PriceLinechartEntity yestPrice = priceLinechartService.selectById(yesterday);
        if (yestPrice == null) {
            yestPrice = list.get(list.size() - 1);
        }
        return R.ok().put("date", date).put("price", price).put("yesterday", yestPrice);
    }

    @PostMapping("getTime")
    public R getTime() {
        return R.ok(String.valueOf(System.currentTimeMillis()));
    }

    @PostMapping("getBitflash")
    public R getBitflash() {
        String value = platFormConfigService.getVal("bitflush_url");
        return R.ok(value);
    }

}
