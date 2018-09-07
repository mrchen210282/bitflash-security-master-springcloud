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
        String one = "1";
        String zero = "0";
        String status ="status";
        Map<String, String> map = new HashMap<>();
        AppStatusEntity appStatusEntity = appStatusService.selectById(appid);
        if (appStatusEntity == null) {
            map.put(status, "-1");
            return R.ok().put("data", map);
        }
        String[] newVersion = appStatusEntity.getVersion().split("\\.");
        String[] appVersion = version.split("\\.");
        map.put(status, one);
        map.put("url", appStatusEntity.getUrl());
        map.put("note", appStatusEntity.getNote());
        map.put("title", appStatusEntity.getTitle());
        //长度以数据库为准时
        /**
         *  state1: 取每一位与数据库对比，只有当app的版本号小于数据库的版本时，才会提示更新
         */
        if (newVersion.length <= appVersion.length) {
            for (int i = 0; i < newVersion.length; i++) {
                int nv = Integer.parseInt(newVersion[i]);
                int av = Integer.parseInt(appVersion[i]);
                //app版本>= 数据库版本，不提示
                if (nv <= av) {
                    map.put(status, zero);
                }else{
                    //app版本< 数据库版本，提示
                    map.put(status, one);
                    return R.ok().put("data", map);
                }
            }
        }
        //长度以app为准时
        /**
         * state1: 数据库版本9.1.14.1，app版本9.1.14,提示更新
         *         对比下来数据是一样的，所以要在最后一次循环判断下之前对比的结果，如果都一样，则证明数据库版本号长且前几位与app版本一直，则提示更新。
         * state2: 数据库版本8.1.14.1，app版本9.1.14，提示不更新
         */
        else {
            boolean flag =false;
            for (int i = 0; i < appVersion.length; i++) {
                int nv = Integer.parseInt(newVersion[i]);
                int av = Integer.parseInt(appVersion[i]);
                if (nv < av) {
                    flag =true;
                    map.put(status, zero);
                }else if(nv == av){
                    map.put(status, zero);
                }else{
                    map.put(status,one);
                    return R.ok().put("data", map);
                }
                if (i == appVersion.length-1 && flag==false) {
                    map.put(status, one);
                    return R.ok().put("data", map);
                }
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
    @PostMapping("getWeekPriceRate")
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
