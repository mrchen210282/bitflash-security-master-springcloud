package cn.bitflash.controller;

import cn.bitflash.annotation.DecodeToken;
import cn.bitflash.annotation.Login;
import cn.bitflash.service.UserAccountService;
import cn.bitflash.user.TokenEntity;
import cn.bitflash.user.UserAccountEntity;
import cn.bitflash.utils.AESTokenUtil;
import cn.bitflash.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * @author chen
 */
@Controller
@RequestMapping("/api/external")
public class ApiExternalController {

    @Autowired
    private UserAccountService userAccountService;

    /**
     *
     * @param token
     * @return count 贝壳数量
     * @throws UnsupportedEncodingException
     */
    @Login
    @ResponseBody
    @PostMapping("getBKCNum")
    public R getBKCNum(@DecodeToken TokenEntity token) throws UnsupportedEncodingException {
        String uid=token.getUid();
        UserAccountEntity accountEntity=userAccountService.selectById(uid);
        String time=String.valueOf(System.currentTimeMillis());
        String count=AESTokenUtil.setData(token.getToken(),accountEntity.getAvailableAssets().toString());
        return R.ok().put("count",count);
    }

    public static void main(String[] args) throws UnsupportedEncodingException{
        String uid="008B73CAD7824679B8E622F40B5227F8";
        String token="6e4b31ae75f749e8814ed820c90aae62";
        String time=String.valueOf(System.currentTimeMillis());
        System.out.println(time);
        System.out.println("加密的token:"+AESTokenUtil.setToken(time,token));
        System.out.println("加密的uid:"+AESTokenUtil.setData(token,uid));
        System.out.println("加密的count:"+AESTokenUtil.setData(token,"100"));
    }


    /**
     *
     * @param token 解密后的token实体类
     * @param count 传递过来的入库数量
     * @return
     * @throws UnsupportedEncodingException
     */
    @Login
    @ResponseBody
    @PostMapping("addBKCNum")
    public R addBKCNum(@DecodeToken TokenEntity token, @RequestParam("count")String count) throws UnsupportedEncodingException{
        String uid=token.getUid();
        UserAccountEntity accountEntity=userAccountService.selectById(uid);
        count=AESTokenUtil.getData(token.getToken(),count);
        System.out.println(count);
        BigDecimal regulateIncome=new BigDecimal(count);
        accountEntity.setRegulateIncome(accountEntity.getRegulateIncome().add(regulateIncome));
        accountEntity.setAvailableAssets(accountEntity.getRegulateIncome().add(accountEntity.getRegulateRelease()));
        userAccountService.updateById(accountEntity);
        return R.ok();
    }

}
