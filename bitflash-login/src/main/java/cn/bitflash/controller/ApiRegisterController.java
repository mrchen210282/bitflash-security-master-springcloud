package cn.bitflash.controller;

import cn.bitflash.annotation.Login;
import cn.bitflash.annotation.LoginUser;
import cn.bitflash.common.Common;
import cn.bitflash.common.utils.R;
import cn.bitflash.common.validator.ValidatorUtils;
import cn.bitflash.entity.*;
import cn.bitflash.login.RegisterForm;
import cn.bitflash.login.UserEntity;
import cn.bitflash.service.TokenService;
import cn.bitflash.service.UserService;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.codec.digest.DigestUtils;
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
@RequestMapping("/api")
public class ApiRegisterController {
	
	private Logger logger = LoggerFactory.getLogger(ApiRegisterController.class);
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserInvitationCodeService userInvitationCodeService;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private AuthorityUserService authorityUserService;
	
	@Autowired
	private UserEmpowerService userEmpowerService;
	
	@Autowired
	private TokenService tokenService;
	
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
		userAccountService.insert(userAccountEntity);
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
			UserInvitationCodeEntity userInvitationCodeEntity = userInvitationCodeService.selectOne(new EntityWrapper<UserInvitationCodeEntity>().eq("lft_code", form.getInvitationCode()).or().eq("rgt_code", form.getInvitationCode()));
			if (userInvitationCodeEntity == null) {
				return R.error("邀请码不正确");
			}
			userinfo.setInvitation(true);
			userinfo.setInvitationCode(form.getInvitationCode());
		}
		userInfoService.insert(userinfo);
		log.info("手机号：" + form.getMobile() + ",注册成功，途径app，没有推广码");
		return R.ok("注册成功");
	}

	/**
	 * @author chen
	 */
	@Transactional
	@GetMapping("register2")
	@ApiOperation("注册")
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
		userAccountService.insert(userAccountEntity);
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
			UserInvitationCodeEntity userInvitationCodeEntity = userInvitationCodeService.selectOne(new EntityWrapper<UserInvitationCodeEntity>().eq("lft_code", invitationCode).or().eq("rgt_code", invitationCode));
			if (userInvitationCodeEntity == null) {
				return R.error("邀请码不正确");
			}
			userinfo.setInvitation(true);
			userinfo.setInvitationCode(invitationCode);
		}
		userInfoService.insert(userinfo);
		log.info("手机号：" + mobile + ",注册成功，通过注册码注册");
		return R.ok("注册成功");

	}

	@RequestMapping("getVerifyCode")
	@ApiOperation("账号(手机号)")
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
	 * @param nickname
	 *            昵称
	 */
	@Login
	@PostMapping("updateNickName")
	@ApiOperation(value = "修改昵称")
	public R updateNickName(@ApiIgnore @LoginUser UserEntity user, @RequestParam String nickname) {
		if (StringUtils.isNotBlank(nickname)) {
			if (nickname.length() <= 6) {
				UserInfoEntity userInfoEntity = userInfoService.selectOne(new EntityWrapper<UserInfoEntity>().eq("nickname", nickname));

				if (null != userInfoEntity && "1".equals(userInfoEntity.getNicklock())) {
					return R.error("昵称不能修改！");
				} else {
					UserInfoEntity userInfoBean = new UserInfoEntity();
					userInfoBean.setNickname(nickname);
					userInfoBean.setUid(user.getUid());
					userInfoBean.setNicklock("1");
					userInfoService.updateById(userInfoBean);
					return R.ok();
				}
			} else {
				return R.error("昵称长度不能大于6个字！");
			}
		} else {
			return R.error("昵称不能为空！");
		}
	}

	/**
	 * @param 授权登录
	 *            昵称
	 */
	@GetMapping("authorityValidate")
	@ApiOperation(value = "校验第三方登录")
	public R authorityValidate(HttpServletRequest request) {
		logger.info("----校验第三方登录------");
		String clientid = request.getParameter("clientid");
		String mobile = request.getParameter("mobile");
		String ticket = request.getParameter("ticket");
		String time = request.getParameter("time");
		String sign = request.getParameter("sign");
		String apiKey = "b1gtuVZRWVh0BdBX";
		
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
						UserAccountEntity userAccountEntity = userAccountService.selectOne(new EntityWrapper<UserAccountEntity>().eq("uid", authorityUserEntity.getUid()));
						if(null != userAccountEntity) {
							availableAssets = userAccountEntity.getAvailableAssets();
						} else {
							availableAssets = new BigDecimal("0.00");
						}
						
						Long timeVal = System.currentTimeMillis();
						List<String> outParam = new ArrayList<String>();
						
						//返回token信息
						TokenEntity tokenEntity = tokenService.selectOne(new EntityWrapper<TokenEntity>().eq("mobile", mobile));
						outParam.add(tokenEntity.getToken());
						outParam.add(authorityUserEntity.getUid());
						outParam.add(availableAssets.toEngineeringString());
						outParam.add(timeVal.toString());
						outParam.add(apiKey);
						String returnSign = Common.returnMD5(outParam);
						
						return R.ok().put("token", tokenEntity.getToken()).put("uid", authorityUserEntity.getUid()).put("availableAssets", availableAssets).put("sign", returnSign);
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
