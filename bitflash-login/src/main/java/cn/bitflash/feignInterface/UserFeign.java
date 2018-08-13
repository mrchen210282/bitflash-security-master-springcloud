package cn.bitflash.feignInterface;


import cn.bitflash.feignInterface.impl.UserFeignFallback;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value="bitflash-user",fallbackFactory = UserFeignFallback.class)
public interface UserFeign {

    /**
     * 查询邀请码
     * @param
     * @return
     */
    @PostMapping("/api/user/inner/selectone")
    UserInvitationCodeEntity selectOne(@RequestParam("invitationCode")String invitationCode);

    /**
     * 初始化user_info表
     * @param userInfoEntity
     * @return
     */
    @PostMapping("api/user/inner/insert")
    boolean insert(@RequestBody UserInfoEntity userInfoEntity);
}
