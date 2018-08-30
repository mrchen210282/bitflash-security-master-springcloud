package cn.bitflash.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.bitflash.exception.RRException;
import cn.bitflash.login.AuthorityUserEntity;
import cn.bitflash.login.RegisterForm;
import cn.bitflash.login.TokenEntity;
import cn.bitflash.login.UserEmpowerEntity;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.AuthorityUserService;
import cn.bitflash.service.TokenService;
import cn.bitflash.service.UserEmpowerService;
import cn.bitflash.service.UserService;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.user.UserInvitationCodeEntity;
import cn.bitflash.utils.Common;
import cn.bitflash.utils.R;
import common.utils.SmsUtils;
import common.validator.ValidatorUtils;

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
	 * @param form
	 *            注册表单
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

		UserAccountGameEntity userAccountGameEntity = new UserAccountGameEntity();
		userAccountGameEntity.setCreateTime(new Date());
		userAccountGameEntity.setUid(uid);
		tradeUtils.insertUserAccountGame(userAccountGameEntity);

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
			UserInvitationCodeEntity userInvitationCodeEntity = userUtils.selectUserInvitationCodeEntity(form.getInvitationCode(),form.getInvitationCode());

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
		boolean flag= tradeUtils.insert(userAccountEntity);
		if(!flag){
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
	 *
	 */
	@GetMapping("authorityValidate")
	public R authorityValidate(HttpServletRequest request) {
		logger.info("----校验第三方登录------");

		String clientid = request.getParameter("clientid");
		System.out.println(clientid);
		String mobile = request.getParameter("mobile");
		String ticket = request.getParameter("ticket");
		String time = request.getParameter("time");
		String sign = request.getParameter("sign");
		String apiKey = "b1gtuVZRWVh0BdBX";

		logger.info(mobile);
		logger.info(ticket);
		logger.info(time);
		logger.info(sign);

		List<String> inParam = new ArrayList<String>();
		inParam.add(mobile);
		inParam.add(ticket);
		inParam.add(clientid);
		inParam.add(time);
		inParam.add(apiKey);

		String mySign = Common.returnMD5(inParam);

		if(sign.equals(mySign)) {
			if(StringUtils.isNotBlank(clientid) && StringUtils.isNotBlank(mobile) && StringUtils.isNotBlank(ticket)) {
				UserEmpowerEntity userEmpowerEntity = userEmpowerService.selectOne(new EntityWrapper<UserEmpowerEntity>().eq("appid", clientid).eq("ticket", ticket));
				logger.info("clientid:" + clientid);

				if(null != userEmpowerEntity) {
					AuthorityUserEntity authorityUserEntity = authorityUserService.selectOne(new EntityWrapper<AuthorityUserEntity>().eq("mobile", mobile));
					if(null != authorityUserEntity) {

						BigDecimal availableAssets = null;
						//UserAccountEntity userAccountEntity = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", authorityUserEntity.getUid()));
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("uid",authorityUserEntity.getUid());
						UserAccountEntity userAccountEntity = tradeUtils.selectOne(map);
						UserInfoEntity userInfoEntity =  userUtils.selectUserInfoById(authorityUserEntity.getUid());

						if(null != userAccountEntity) {
							availableAssets = userAccountEntity.getAvailableAssets();
						} else {
							availableAssets = new BigDecimal("0.00");
						}

						Long timeVal = System.currentTimeMillis();
						List<String> outParam = new ArrayList<String>();

						String nickname = "";
						if(null != userInfoEntity) {
							nickname = userInfoEntity.getNickname();
						}

						//返回token信息
						TokenEntity tokenEntity = tokenService.selectOne(new EntityWrapper<TokenEntity>().eq("mobile", mobile));
						outParam.add(tokenEntity.getToken());
						outParam.add(authorityUserEntity.getUid());
						outParam.add(availableAssets.toEngineeringString());
						outParam.add(nickname);
						outParam.add(timeVal.toString());
						outParam.add(apiKey);
						String returnSign = Common.returnMD5(outParam);

						return R.ok().put("token", tokenEntity.getToken()).put("uid", authorityUserEntity.getUid()).put("availableAssets", availableAssets).put("sign", returnSign).put("nickname", nickname);
					} else {
						return R.error();
					}
				}
			}
		}
		return R.error();
	}

	private String generateCode() {
		return String.valueOf(new Random().nextInt(8999) + 1000);
	}

	private String generateUUID32() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	public static void main(String arg[]) {
		for (int i = 0; i < 10; i++)
			System.out.println(UUID.randomUUID().toString().replace("-", "").toUpperCase());
		System.out.println(String.valueOf(new Random().nextInt(8999) + 1000));

		System.out.println(DigestUtils.sha256Hex("123456"));

	}

	public String getName() {
		String str = "用户";
		int max = 10000;
		int min = 1000;
		Random random = new Random();
		str += random.nextInt(max) % (max - min + 1) + min;
		return str;
	}
}
