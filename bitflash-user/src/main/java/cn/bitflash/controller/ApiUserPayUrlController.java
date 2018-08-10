package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.feign.TradeFeign;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.UserInfoService;
import cn.bitflash.service.UserPayUrlService;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserPayUrlEntity;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api" )
//@Api(tags = "上传图片" )
public class ApiUserPayUrlController {

    @Autowired
    private UserPayUrlService userPayUrlService;

    @Autowired
    private TradeFeign tradeFeign;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 上传的图片
     * @param img     上传的图片
     * @param imgType 图片类型(微信:1,支付宝:2,身份证正面：3，身份证反面：4)
     * @param
     * @return
     */
    @Login
    @PostMapping("upload" )
    public R upload(@RequestParam String img, @RequestParam String imgType, @LoginUser UserEntity user) {

        String imgUrl = "";
        String path = "/home/statics/qrcode/";

        String md5 = MD5Util.stringToMD5(user.getMobile() + System.currentTimeMillis());
        if (imgType.equals("1" )) {
            String md5_w = md5 + "_w";
            path = path + md5_w + ".png";
            imgUrl = "http://www.bitflash.vip/qrcode/" + md5_w + ".png";
        } else if (imgType.equals("2" )) {
            String md5_z = md5 + "_z";
            path = path + md5_z + ".png";
            imgUrl = "http://www.bitflash.vip/qrcode/" + md5_z + ".png";
        }


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
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mobile", user.getMobile());
        map.put("imgType", imgType);
        List<UserPayUrlEntity> list = userPayUrlService.selectUserPayUrlByParam(map);
        if (null != list && list.size() > 0) {
            map.put("imgType", imgType);
            map.put("imgUrl", imgUrl);
            map.put("mobile", user.getMobile());
            userPayUrlService.updateUserPayUrl(map);
        } else {
            UserPayUrlEntity userPayUrl = new UserPayUrlEntity();
            userPayUrl.setImgType(imgType);
            userPayUrl.setUid(user.getUid());
            userPayUrl.setImgUrl(imgUrl);
            userPayUrl.setMobile(user.getMobile());
            userPayUrl.setCrateTime(new Date());
            userPayUrlService.insert(userPayUrl);
        }
        return R.ok();
    }

    /**
     * 取得上传的图片
     * @param user
     * @return
     */
    @Login
    @PostMapping("userInfoImg" )
    public R userInfoImg(@LoginUser UserEntity user) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mobile", user.getMobile());
        List<Map<String, Object>> list = tradeFeign.selectTradeUrl(map);
        Map<String, Object> returnMap1 = null;
        if (null != list && list.size() > 0) {
            returnMap1 = list.get(0);
            returnMap1.put("wechatUrl", returnMap1.get("imgUrl" ));
            if (list.size() > 1) {
                Map<String, Object> returnMap2 = list.get(1);
                if (null != returnMap2.get("imgUrl" )) {
                    returnMap1.put("payUrl", returnMap2.get("imgUrl" ));
                }
            }
        }
        return R.ok().put("payMap", returnMap1);

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
     * 上传支付宝/微信图片
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


}
