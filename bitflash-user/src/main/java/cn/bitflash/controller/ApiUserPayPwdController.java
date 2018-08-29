package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.service.UserPayPwdService;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 用户交易密码
 * @author chen
 */
@RestController
@RequestMapping("/api/payPwd")
public class ApiUserPayPwdController {

    @Autowired
    private UserPayPwdService userPayPwdService;

    /**
     * 添加交易密码
     * @param payPassword 交易密码
     * @author chen
     */
    @Login
    @PostMapping("addPayPwd" )
    public R addPayPassword(@RequestParam String payPassword, @RequestAttribute("uid")String uid) {
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

}
