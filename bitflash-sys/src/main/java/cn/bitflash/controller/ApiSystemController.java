package cn.bitflash.controller;

import cn.bitflash.service.AppStatusService;
import cn.bitflash.system.AppStatusEntity;
import cn.bitflash.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author soso
 * @date 2018年5月22日 下午3:12:11
 */
@RestController
@RequestMapping("/api" )
public class ApiSystemController {

    @Autowired
    private AppStatusService appStatusService;

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

}
