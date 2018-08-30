package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.login.UserEntity;
import cn.bitflash.loginutil.LoginUtils;
import cn.bitflash.service.UserPayPwdService;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
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

    @Autowired
    private LoginUtils loginUtils;

    /**
     * 添加交易密码
     * @param newPwd 新密码
     * @param uid uid
     * @return
     */
    @Login
    @PostMapping("addOrUpdatePayPwd")
    public R addPayPassword(@RequestParam String newPwd,@RequestAttribute("uid")String uid ) {
        UserPayPwdEntity userPayPwdEntity = userPayPwdService.selectOne(new EntityWrapper<UserPayPwdEntity>().eq("uid", uid));
        //第一次添加交易密码
        if (userPayPwdEntity == null) {
            userPayPwdEntity = new UserPayPwdEntity();
            userPayPwdEntity.setUid(uid);
            userPayPwdEntity.setCreateTime(new Date());
            userPayPwdEntity.setPayPassword(newPwd);
            userPayPwdService.insert(userPayPwdEntity);
            return R.ok();
        }
        //更新交易密码
        userPayPwdEntity.setPayPassword(newPwd);
        userPayPwdService.updateById(userPayPwdEntity);
        return R.ok();
    }

    @Login
    @PostMapping("vaildatePwd233")
    public R  vaildatePwd233(@RequestParam String oldPwd,@RequestParam String loginPwd,@RequestAttribute("uid")String uid){
        UserEntity userEntity = loginUtils.selectOneByUser(new ModelMap("uid",uid));
        if(!userEntity.getPassword().equals(loginPwd)){
            return R.error(501,"登录密码错误");
        }
        UserPayPwdEntity userPayPwdEntity = userPayPwdService.selectOne(new EntityWrapper<UserPayPwdEntity>().eq("uid", uid));
        if(!oldPwd.equals(userPayPwdEntity.getPayPassword())){
            return R.error(502,"交易密码错误");
        }
        return R.ok();

    }


}
