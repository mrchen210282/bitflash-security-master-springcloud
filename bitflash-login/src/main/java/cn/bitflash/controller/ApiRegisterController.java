package cn.bitflash.controller;

import cn.bitflash.exception.RRException;
import cn.bitflash.login.*;
import cn.bitflash.service.AuthorityUserService;
import cn.bitflash.service.TokenService;
import cn.bitflash.service.UserEmpowerService;
import cn.bitflash.service.UserService;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.tradeutil.TradeUtils;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import cn.bitflash.userutil.UserUtils;
import cn.bitflash.utils.Common;
import cn.bitflash.utils.R;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import common.utils.SmsUtils;
import common.validator.ValidatorUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/reg")
public class ApiRegisterController {

    private Logger logger = LoggerFactory.getLogger(ApiRegisterController.class);

    @Autowired
    private UserService userService;


    @Autowired
    private AuthorityUserService authorityUserService;


    @Autowired
    private UserEmpowerService userEmpowerService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private TradeUtils tradeUtils;

    private final Logger log = LoggerFactory.getLogger(ApiRegisterController.class);

    /**
     * @param form 注册表单
     * @author chen
     */
    @Transactional
    @PostMapping("register")
    public R register(@RequestBody RegisterForm form, HttpServletResponse response) {
        UserEntity oldUser = userService.selectById(form.getMobile());
        if (oldUser != null) {
            return R.error(501, "手机号已经存在");
        }
        ValidatorUtils.validateEntity(form);
        String name = this.getName();
        String uid = generateUUID32();
        // 初始化tb_user表
        UserEntity user = new UserEntity();
        user.setUid(uid);
        user.setMobile(form.getMobile());
        user.setPassword(form.getPwd());
        user.setUuid(generateUUID32());

        userService.insert(user);
        // 初始化user_account表
        UserAccountEntity userAccountEntity = new UserAccountEntity();
        userAccountEntity.setCreateTime(new Date());
        userAccountEntity.setUid(uid);
        tradeUtils.insert(userAccountEntity);

//		UserAccountGameEntity userAccountGameEntity = new UserAccountGameEntity();
//		userAccountGameEntity.setCreateTime(new Date());
//		userAccountGameEntity.setUid(uid);
//		tradeUtils.insertUserAccountGame(userAccountGameEntity);

        // 初始化user_info表
        UserInfoEntity userinfo = new UserInfoEntity();
        userinfo.setUid(uid);
        userinfo.setIsAuthentication("0");
        userinfo.setIsVip("0");
        userinfo.setMobile(form.getMobile());
        userinfo.setInvitation(false);
        userinfo.setRealname(name);
        userinfo.setNickname(name);
        if (StringUtils.isNotBlank(form.getInvitationCode())) {
            // 校验验证码是否正确
            UserInvitationCodeEntity userInvitationCodeEntity = userUtils.selectUserInvitationCodeEntity(form.getInvitationCode(), form.getInvitationCode());

            if (userInvitationCodeEntity == null) {
                return R.error("邀请码不正确");
            }
            userinfo.setInvitation(true);
            userinfo.setInvitationCode(form.getInvitationCode());
        }

        userUtils.insert(userinfo);
        log.info("手机号：" + form.getMobile() + ",注册成功，途径app，没有推广码");
        return R.ok("注册成功");
    }

    /**
     * @author chen
     */
    @Transactional
    @GetMapping("register2")
    public R register2(@RequestParam String mobile, @RequestParam String pwd, @RequestParam(value = "invitationCode", required = false) String invitationCode, HttpServletResponse response) {
        // ValidatorUtils.validateEntity(form);
        response.addHeader("Access-Control-Allow-Origin", "*");
        String name = this.getName();
        String uid = generateUUID32();
        // 初始化tb_user表
        UserEntity user = new UserEntity();
        user.setUid(uid);
        user.setMobile(mobile);
        user.setPassword(pwd);
        user.setUuid(generateUUID32());

        userService.insert(user);
        // 初始化user_account表
        UserAccountEntity userAccountEntity = new UserAccountEntity();
        userAccountEntity.setCreateTime(new Date());
        userAccountEntity.setUid(uid);
        boolean flag = tradeUtils.insert(userAccountEntity);
        if (!flag) {
            throw new RRException("初始化用户失败");
        }
        // 初始化user_info表
        UserInfoEntity userinfo = new UserInfoEntity();
        userinfo.setUid(uid);
        userinfo.setIsAuthentication("0");
        userinfo.setIsVip("0");
        userinfo.setMobile(mobile);
        userinfo.setInvitation(false);
        userinfo.setNickname(name);
        userinfo.setRealname(name);
        if (StringUtils.isNotBlank(invitationCode)) {
            // 校验验证码是否正确
            UserInvitationCodeEntity userInvitationCodeEntity = userUtils.selectOne(invitationCode);
            if (userInvitationCodeEntity == null) {
                return R.error("邀请码不正确");
            }
            userinfo.setInvitation(true);
            userinfo.setInvitationCode(invitationCode);
        }
        userUtils.insert(userinfo);
        log.info("手机号：" + mobile + ",注册成功，通过注册码注册");
        return R.ok("注册成功");

    }

    @RequestMapping("getVerifyCode")
    public R sent(@RequestParam String mobile, @RequestParam String type, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (StringUtils.isBlank(mobile)) {
            return R.error(501, "手机号不能为空");
        }
        UserEntity userEntity = userService.selectOne(new EntityWrapper<UserEntity>().eq("mobile", mobile));
        String verifyCode = generateCode();
        if (type.equals("reg")) {
            if (userEntity != null) {
                return R.error("用户已注册，请直接登录");
            }
            return SmsUtils.smsApi(mobile, verifyCode, "贝壳", "SMS_135330146");
        }
        // 找回密码
        if (type.equals("findPwd")) {
            if (userEntity == null) {
                return R.error("手机号错误");
            }
            return SmsUtils.smsApi(mobile, verifyCode, "贝壳", "SMS_136065023");
        }
        return R.error("系统错误");
    }

    /**
     * @param
     */
    @GetMapping("authorityValidate")
    public R authorityValidate(HttpServletRequest request) {
        logger.info("----校验第三方登录------");

        String clientid = request.getParameter("clientid");
        String mobile = request.getParameter("mobile");
        String ticket = request.getParameter("ticket");
        String time = request.getParameter("time");
        String sign = request.getParameter("sign");
        String apiKey = "b1gtuVZRWVh0BdBX";

        logger.info(clientid);
        logger.info(mobile);
        logger.info(ticket);
        logger.info(time);
        logger.info(sign);

        List<String> inParam = new ArrayList<>();
        inParam.add(mobile);
        inParam.add(ticket);
        inParam.add(clientid);
        inParam.add(time);
        inParam.add(apiKey);

        String mySign = Common.returnMD5(inParam);
        System.out.println("-------------" + mySign);
        if (sign.equals(mySign)) {
            if (StringUtils.isNotBlank(clientid) && StringUtils.isNotBlank(mobile) && StringUtils.isNotBlank(ticket)) {
                UserEmpowerEntity userEmpowerEntity = userEmpowerService.selectOne(new EntityWrapper<UserEmpowerEntity>().eq("appid", clientid).eq("ticket", ticket));
                logger.info("clientid:" + clientid);

                if (null != userEmpowerEntity) {
                    AuthorityUserEntity authorityUserEntity = authorityUserService.selectOne(new EntityWrapper<AuthorityUserEntity>().eq("mobile", mobile));
                    if (null != authorityUserEntity) {

                        BigDecimal availableAssets = null;
                        //UserAccountEntity userAccountEntity = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", authorityUserEntity.getUid()));
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("uid", authorityUserEntity.getUid());
                        UserAccountEntity userAccountEntity = tradeUtils.selectOne(map);
                        UserInfoEntity userInfoEntity = userUtils.selectUserInfoById(authorityUserEntity.getUid());

                        if (null != userAccountEntity) {
                            availableAssets = userAccountEntity.getAvailableAssets();
                        } else {
                            availableAssets = new BigDecimal("0.00");
                        }

                        Long timeVal = System.currentTimeMillis();
                        List<String> outParam = new ArrayList<>();

                        String nickname = "";
                        if (null != userInfoEntity) {
                            nickname = userInfoEntity.getNickname();
                        }

//						Integer intAvailableAssets = new Integer(availableAssets.toString());
                        //返回token信息
                        TokenEntity tokenEntity = tokenService.selectOne(new EntityWrapper<TokenEntity>().eq("mobile", mobile));
                        outParam.add(tokenEntity.getToken());
                        outParam.add(authorityUserEntity.getUid());
                        outParam.add(availableAssets.toString());
                        outParam.add(nickname);
                        outParam.add(timeVal.toString());
                        outParam.add(apiKey);
                        String returnSign = Common.returnMD5(outParam);

                        logger.info("availableAssets:" + availableAssets.toString());
                        logger.info("uid:" + authorityUserEntity.getUid());
                        logger.info("nickname:" + nickname);

                        return R.ok().put("token", tokenEntity.getToken()).put("uid", authorityUserEntity.getUid())
                                .put("availableAssets", availableAssets.toString()).put("sign", returnSign).put("nickname", nickname).put("code", "0");
                    } else {
                        return R.error();
                    }
                }
            }
        } else {
            logger.info("密钥错误！");
        }
        return R.error();
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(8999) + 1000);
    }

    private String generateUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

//	public static void main(String arg[]) {
//		for (int i = 0; i < 10; i++)
//			System.out.println(UUID.randomUUID().toString().replace("-", "").toUpperCase());
//		System.out.println(String.valueOf(new Random().nextInt(8999) + 1000));
//
//		System.out.println(DigestUtils.sha256Hex("123456"));
//
//	}


    public String getName() {
        String str = "用户";
        int max = 10000;
        int min = 1000;
        Random random = new Random();
        str += random.nextInt(max) % (max - min + 1) + min;
        return str;
    }

    public static void main(String[] args) {

        // String sb = "http://localhost:52714//api/reg/authorityValidate?" +
        //   "clientid=1D09E95EAE5F4B6D82683D5BB45681A8&mobile=13846756663&ticket=GR5DH97PEW&" +
        // "time=1537006861816&sign=8383d79c95bb07aa8cff5968252ada89";


        String sb = "http://localhost:52716/api/external/changeBKCNum?" +
                "uid=DF3223E855254FB08B4B8A92EA84C230&flag=0&" +
                "time=GR5DH97PEW&sign=7ed81e67aac435ce34bb0efe1c5e1de3&bkcNum=19";
        String content = HttpUtil.sendHttpData(sb.toString(), 3000);
        System.out.println("url=" + sb.toString());
        System.out.println(content);
        String msg = "";
        int code = 0;
        int availableAssets = 0;
        JSONObject jsObject = JSONObject.parseObject(content);
        msg = jsObject.getString("msg");
        code = Integer.valueOf(jsObject.getString("code"));
        if (msg.equals("success"))
            availableAssets = new Float(jsObject.getString("availableAssets")).intValue();
        System.out.println("availableAssets=" + availableAssets);
        if (msg.equals("success") && code == 1) {
            System.out.println("code=" + code);
        }

    }
}
