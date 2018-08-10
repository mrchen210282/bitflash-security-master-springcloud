package cn.bitflash.feign;


import cn.bitflash.login.UserEntity;
import cn.bitflash.prod.DefaultFallBack.UserDefaultFallBackFactory;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * name为资源提供者的spring.application.name
 * @author eric
 */
@FeignClient(value="bitflash-user",fallbackFactory = UserDefaultFallBackFactory.class)
public interface UserRibbon {
    /**
     *获取账户信息
     * @return
     */

    @GetMapping("/api/userInfo")
    public R userInfo();

    @GetMapping("/api/accountInfo")
    public R accountInfo();

    @PostMapping("/api/getInvitationCode")
    public R getInvitationcode();

    @PostMapping("/api/getRelation")
    public R getRelation();

    @PostMapping("/api/changePassword")
    public R changePwd(@RequestParam String oldPwd, @RequestParam String newPwd);

    @PostMapping("/api/changePassword2")
    public R changePwd2(@RequestParam String mobile, @RequestParam String newPwd);

    @PostMapping("updateNickName")
    public R updateNickName(@RequestParam String nickname);


    /**
     * 用户交易密码
     */

    @PostMapping("/api/payPwd/addPayPwd")
    public R addPayPassword(@RequestParam String payPassword);

    @PostMapping("/api/payPwd/updatePayPwd")
    public R updatePayPwd(@RequestParam String oldPwd, @RequestParam String newPwd);


    /**
     * 获取用户vip信息
     */
    @PostMapping("/api/vip/getVipLevel")
    public R getVipLevel();

    @PostMapping("/api/vip/updateVipLevel")
    public R updateVipLevel();


    /**
     * 公用
     */
    @PostMapping("/user/withinCode/selectone")
    public UserInvitationCodeEntity selectOne(@RequestBody EntityWrapper entityWrapper);

    @PostMapping("/user/withinInfo/insert")
    public boolean insert(@RequestBody UserInfoEntity userInfoEntity);
}
