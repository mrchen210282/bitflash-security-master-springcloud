package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.annotation.PayPassword;
import cn.bitflash.service.UserPayPwdService;
import cn.bitflash.user.UserEntity;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author chen
 */
@RestController
@RequestMapping("/api/payPwd" )
//@Api(tags = "用户交易密码" )
public class ApiUserPayPwdController {

    @Autowired
    private UserPayPwdService userPayPwdService;

    /**
     * @param payPassword 交易密码
     * @author chen
     */
    @Login
    @PostMapping("addPayPwd" )
   // @ApiOperation("添加交易密码" )
    public R addPayPassword(@RequestParam String payPassword, @LoginUser UserEntity user) {
        String uid = user.getUid();
        UserPayPwdEntity userPayPwdEntity = userPayPwdService.selectOne(new EntityWrapper<UserPayPwdEntity>().eq("uid", uid));
        if (userPayPwdEntity == null) {
            userPayPwdEntity = new UserPayPwdEntity();
            userPayPwdEntity.setUid(uid);
            userPayPwdEntity.setCreateTime(new Date());
            userPayPwdEntity.setPayPassword(payPassword);
            userPayPwdService.insert(userPayPwdEntity);
            //第一次添加交易密码
            return R.ok();
        }
        userPayPwdEntity.setPayPassword(payPassword);
        userPayPwdService.updateById(userPayPwdEntity);
        //更新交易密码
        return R.ok();

    }

    /**
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @author chen
     */
    @Login
    @PostMapping("updatePayPwd" )
    //@ApiOperation("更新交易密码" )
    public R updatePayPwd(@PayPassword UserPayPwdEntity payPwd, @RequestParam String oldPwd, @RequestParam String newPwd) {
        if (payPwd != null) {
            if (oldPwd.equals(payPwd.getPayPassword())) {
                payPwd.setPayPassword(newPwd);
                userPayPwdService.updateById(payPwd);
                return R.ok();
            } else {
                return R.error("原始密码不正确" );
            }
        }
        return R.error("未初始化密码" );
    }
}
