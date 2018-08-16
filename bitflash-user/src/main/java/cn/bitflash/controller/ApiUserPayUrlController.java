package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.feignInterface.TradeFeign;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.UserInfoService;
import cn.bitflash.service.UserPayPwdService;
import cn.bitflash.service.UserPayUrlService;
import cn.bitflash.trade.UserTradeEntity;
import cn.bitflash.user.ImgForm;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserPayPwdEntity;
import cn.bitflash.user.UserPayUrlEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.utils.MD5Util;
import common.validator.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api" )
public class ApiUserPayUrlController {

    @Autowired
    private UserPayUrlService userPayUrlService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserPayPwdService userPayPwdService;

    @Autowired
    private TradeFeign tradeFeign;

    /**
     * 上传的图片
     * @param imgForm 图片类型(微信:1,支付宝:2,身份证正面：3，身份证反面：4，银行卡：5)
     * @param
     * @return
     */
    @Login
    @PostMapping("upload")
    public R upload(@RequestBody ImgForm imgForm,  @LoginUser UserEntity user) {
        ValidatorUtils.validateEntity(imgForm);
        //验证交易密码
        String password = imgForm.getPassword();
        UserPayPwdEntity pwdEntity = userPayPwdService.selectOne(new EntityWrapper<UserPayPwdEntity>().eq("uid",user.getUid()));
        if(!password.equals(pwdEntity.getPayPassword())){
            return R.error("交易密码错误");
        }

        String imgType = imgForm.getImgType();
        String imgUrl = "";
        String path = "/home/statics/qrcode/";
        String md5 = MD5Util.stringToMD5(user.getMobile() + System.currentTimeMillis());
        switch (imgType){
            case "1":
                String md5_w = md5 + "_w";
                path = path + md5_w + ".png";
                imgUrl = "http://www.bitflash.vip/qrcode/" + md5_w + ".png";
                break;
            case "2":
                String md5_z = md5 + "_z";
                path = path + md5_z + ".png";
                imgUrl = "http://www.bitflash.vip/qrcode/" + md5_z + ".png";
                break;
            case "5":
                String md5_c = md5 +"_c";
                path = path + md5_c + ".png";
                imgUrl = "http://www.bitflash.vip/qrcode/" + md5_c + ".png";
                break;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            String[] base64Str = imgForm.getImg().split("," );
            byte[] b = decoder.decodeBuffer(base64Str[1]);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            // String imgFilePath = "D:\\upload\\img\\new.jpg";// 新生成的图片
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();

        } catch (Exception e) {
            //return R.error();
        }

        // 先查询是否已上传过图片，如果已上传则使用最新上传的图片
        UserPayUrlEntity userPayUrlEntity = userPayUrlService.selectOne(new EntityWrapper<UserPayUrlEntity>()
                .eq("uid",user.getUid()).eq("img_type",imgType));
        if (userPayUrlEntity==null || userPayUrlEntity.getId()==null) {
            userPayUrlEntity = new UserPayUrlEntity();
            userPayUrlEntity.setImgType(imgType);
            userPayUrlEntity.setAccount(imgForm.getAccount());
            userPayUrlEntity.setCrateTime(new Date());
            userPayUrlEntity.setImgUrl(imgUrl);
            userPayUrlEntity.setMobile(user.getMobile());
            userPayUrlEntity.setName(imgForm.getName());
            userPayUrlEntity.setUid(user.getUid());
            userPayUrlService.insert(userPayUrlEntity);
        } else {
            userPayUrlEntity.setImgUrl(imgUrl);
            userPayUrlEntity.setAccount(imgForm.getAccount());
            userPayUrlEntity.setName(imgForm.getName());
            userPayUrlService.updateById(userPayUrlEntity);
        }
        return R.ok();
    }

    /**
     * 取得上传的微信/支付宝图片
     * @param user
     * @return
     */
    @Login
    @PostMapping("userInfoImg")
    public R userInfoImg(@LoginUser UserEntity user) {
        String uid = user.getUid();
        List<UserPayUrlEntity> list = userPayUrlService.selectList(new EntityWrapper<UserPayUrlEntity>().eq("uid",uid)
                .eq("img_type",1).or().eq("uid",uid).eq("img_type",2));
           Map<String,String> map=new HashMap<>();
           if(list.size()>=1){
               map.put("wechatUrl",list.get(0).getImgUrl());
               map.put("payUrl",list.get(1).getImgUrl());
           }
        return R.ok().put("payMap", map);

    }

    /**
     * 上传身份证图片信息
     *
     * @param img     图片
     * @param imgType 图片类型
     * @author chen
     */
    @Login
    @PostMapping("uploadImg" )
    public R uploadImgMessage(@RequestParam String img, @RequestParam String imgType,
                              @LoginUser UserEntity user) {
        UserPayUrlEntity userPay = userPayUrlService.selectOne(new EntityWrapper<UserPayUrlEntity>().eq("uid", user.getUid())
                .and().eq("img_type", imgType));
        String imgName = imgType.equals("3" ) ? MD5Util.stringToMD5(user.getMobile() + System.currentTimeMillis()) + "_z" : MD5Util.stringToMD5(user.getMobile() + System.currentTimeMillis()) + "_f";
        String imgUrl = "";
        String path = "/home/statics/idnumber/" + imgName + ".png";
        imgUrl = "http://www.bitflash.vip/auth/" + imgName + ".png";
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            String[] base64Str = img.split("," );
            byte[] b = decoder.decodeBuffer(base64Str[1]);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //为空代表第一次上传图片插入操作
        if (userPay == null) {
            userPay = new UserPayUrlEntity();
            userPay.setImgType(imgType);
            userPay.setCrateTime(new Date());
            userPay.setUid(user.getUid());
            userPay.setImgUrl(imgUrl);
            userPay.setMobile(user.getMobile());
            userPayUrlService.insert(userPay);
        } else {
            userPay.setImgUrl(imgUrl);
            userPayUrlService.updateById(userPay);
        }
        UserInfoEntity userinfo = new UserInfoEntity();
        userinfo.setIsAuthentication("1" );
        userinfo.setUid(user.getUid());
        userInfoService.updateById(userinfo);
        return R.ok();
    }

    /**
     * 支付宝/微信图片
     * @param imgType 图片类型
     * @author chen
     */
    @Login
    @PostMapping("getPictureUrl" )
    public R getSFZAdress(@LoginUser UserEntity userEntity, @RequestParam String imgType) {
        List<UserPayUrlEntity> userPay;
        if (imgType.equals("1" )) {
            userPay = userPayUrlService.selectList(new EntityWrapper<UserPayUrlEntity>().eq("uid", userEntity.getUid())
                    .and().eq("img_type", imgType).or().eq("img_type", "2" ).eq("uid", userEntity.getUid()));
            if (userPay == null || userPay.size() == 0) {
                return R.ok();
            }
            Map<String, Object> map = new HashMap<>();
            if (userPay.get(0).getImgType().equals("1" )) {
                map.put("wxPay", userPay.get(0).getImgUrl());
                map.put("alipay", userPay.get(1).getImgUrl());
            } else if (userPay.get(0).getImgType().equals("2" )) {
                map.put("wxPay", userPay.get(1).getImgUrl());
                map.put("alipay", userPay.get(0).getImgUrl());
            }

            return R.ok(map);

        }
        return R.error("身份证接口暂未开放" );
    }

    /**
     * 获取支付信息
     * @param uid
     * @return
     */
    @Login
    @PostMapping("getPayUrl")
    public R getPayUrl(@RequestParam("accountId")String accountId){
        UserTradeEntity tradeEntity = tradeFeign.selectOneTrade(new ModelMap("id",accountId));
        String uid = tradeEntity.getUid();
        List<UserPayUrlEntity> payUrlEntities = userPayUrlService.selectList(new EntityWrapper<UserPayUrlEntity>()
                .eq("uid",uid).eq("img_type",1)
                .or().eq("uid",uid).eq("img_type",2)
                .or().eq("uid",uid).eq("img_type",5));
        if(payUrlEntities == null || payUrlEntities.size()==0){
            return R.error("未设置支付信息");
        }
        Map<String,Object> map= new HashMap<>();
        map.put("wxpay",0);
        map.put("alipay",0);
        map.put("cnypay",0);
        payUrlEntities.stream().forEach(u->{
            if(u.getImgType().equals("1")){
                map.put("wxpay",1);
            }
            if(u.getImgType().equals("2")){
                map.put("alipay",1);
            }
            if(u.getImgType().equals("5")){
                map.put("cny",1);
            }
        });

        return R.ok().put("url",map);
    }

}
