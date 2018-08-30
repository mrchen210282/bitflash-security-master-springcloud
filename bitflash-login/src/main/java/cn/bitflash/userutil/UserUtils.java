package cn.bitflash.userutil;


import cn.bitflash.user.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value="bitflash-user",fallbackFactory = UserFeignFallback.class)
public interface UserUtils {

    /**
     * 查询邀请码
     * @param
     * @return
     */
    @PostMapping("/api/user/inner/selectone")
    UserInvitationCodeEntity selectOne(@RequestParam("invitationCode")String invitationCode);


    /**
     * 查询邀请码
     * @param
     * @return
     */
    @PostMapping("/api/user/inner/selectUserInvitationCodeEntity")
    UserInvitationCodeEntity selectUserInvitationCodeEntity(@RequestParam("lftCode")String lftCode,@RequestParam("rgtCode")String rgtCode);

    /**
     * 初始化user_info表
     * @param userInfoEntity
     * @return
     */
    @PostMapping("api/user/inner/insert")
    boolean insert(@RequestBody UserInfoEntity userInfoEntity);


    /**
     * 用户信息
     * @param
     * @return
     */
    @PostMapping("/api/user/inner/selectUserInfoById")
    UserInfoEntity selectUserInfoById(String uid);


    /**
     * 用户信息
     * @param
     * @return
     */
    @PostMapping("/api/user/inner/selectUserInfoList")
    List<UserInfoEntity> selectUserInfoList( Map<String,Object> map);
}
