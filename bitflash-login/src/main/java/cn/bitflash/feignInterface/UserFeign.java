package cn.bitflash.feignInterface;


import cn.bitflash.feignInterface.impl.UserFeignFallback;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value="bitflash-user",fallbackFactory = UserFeignFallback.class)
public interface UserFeign {

    /**
     * 查询邀请码
     * @param wrapper
     * @return
     */
    @PostMapping("/api/user/withinCode/selectone")
    UserInvitationCodeEntity selectOne(@RequestBody Wrapper wrapper);

    /**
     * 初始化user_info表
     * @param userInfoEntity
     * @return
     */
    @PostMapping("api/user/withinInfo/insert")
    boolean insert(@RequestBody UserInfoEntity userInfoEntity);
}
