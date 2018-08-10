package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.feignInterface.UserLoginFeign;
import cn.bitflash.login.UserGTCidEntity;
import cn.bitflash.service.PlatFormConfigService;
import cn.bitflash.utils.Common;
import cn.bitflash.utils.R;
import cn.bitflash.utils.RedisUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.utils.GeTuiSendMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/msg" )
public class ApiSendMessageController {

    @Autowired
    private UserLoginFeign userLoginFeign;

    @Autowired
    private PlatFormConfigService platFormConfigService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * @param uid 卖家uid
     * @return
     */
    @Login
    @PostMapping("singleMsg" )
    public R sendSingleMsg(@RequestParam String uid, @RequestParam String id) throws Exception {
        String idVal = redisUtils.get(Common.ADD_LOCK+id);
        if (StringUtils.isBlank(idVal)) {
            try {
                UserGTCidEntity gtCidEntity = userLoginFeign.selectOneByGT(new EntityWrapper<UserGTCidEntity>().eq("uid", uid));
                String text = platFormConfigService.getVal(Common.MSG_TEXT);
                GeTuiSendMessage.sendSingleMessage(text, gtCidEntity.getCid());
                redisUtils.set(Common.ADD_LOCK+id, id, 60 * 60);
                return R.ok();
            } catch (NullPointerException e) {
                e.printStackTrace();
                return R.error(502, "对方未登录最新App，无法催单" );
            }

        }
        return R.error(501, "未超时" );


    }
}
