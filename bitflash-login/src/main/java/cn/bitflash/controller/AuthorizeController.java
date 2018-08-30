package cn.bitflash.controller;

import cn.bitflash.login.AuthorityUserEntity;
import cn.bitflash.login.UserEmpowerEntity;
import cn.bitflash.service.AuthorityUserService;
import cn.bitflash.service.UserEmpowerService;
import cn.bitflash.user.UserInfoEntity;
import cn.bitflash.userutil.UserUtils;
import cn.bitflash.utils.ExternalMD5;
import cn.bitflash.utils.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangjun
 * @version 2018年7月18日下午5:00:46
 *
 */
@Controller
@RequestMapping("/server2")
public class AuthorizeController {

    private final Logger logger = LoggerFactory.getLogger(AuthorizeController.class);

    @Autowired
    private UserEmpowerService userEmpowerService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private AuthorityUserService authorityUserService;


    // 向客户端返回授权许可码 code
    @GetMapping("/responseCode")
    @ResponseBody
    public R toShowUser(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        logger.info("----------服务端/responseCode--------------------------------------------------------------");

        try {
            // 构建OAuth授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            String clientid = oauthRequest.getClientId();
            //String responseType = oauthRequest.getResponseType();
            String url = oauthRequest.getRedirectURI();
            //String clientSecret = request.getParameter("clientSecret");

            //String redirectUrl = request.getParameter("redirectUrl");

            String mobile = request.getParameter("mobile");
            // clientid = request.getParameter("appid");
            // responseType = request.getParameter("responseType");

            logger.info("回调地址为：" + url);
            logger.info("clientid：" + clientid);

            UserEmpowerEntity userEmpowerEntity = userEmpowerService.selectOne(new EntityWrapper<UserEmpowerEntity>().eq("appid", clientid));
            if (null != userEmpowerEntity) {
                // 判断客户端id和
                // if (clientid.equals("bkc") && responseType.equals("code")) {

                // 设置授权码
                // String authorizationCode = "bkcCode";

                Map<String,Object> map = new HashMap<String,Object>();
                map.put("mobile",mobile);
                List<UserInfoEntity> list = userUtils.selectUserInfoList(map);

                if(null != list) {
                    UserInfoEntity userInfoEntity = list.get(0);
                    AuthorityUserEntity AuthorityUser = authorityUserService.selectOne(new EntityWrapper<AuthorityUserEntity>().eq("uid", userInfoEntity.getUid()));
                    if(null == AuthorityUser) {
                        AuthorityUserEntity authorityUserEntity = new AuthorityUserEntity();
                        authorityUserEntity.setMobile(mobile);
                        authorityUserEntity.setTicket(userEmpowerEntity.getTicket());
                        authorityUserEntity.setUid(userInfoEntity.getUid());
                        authorityUserService.insert(authorityUserEntity);
                    }

                    // 进行OAuth响应构建
                    OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
                    // 设置授权码
                    builder.setCode(userEmpowerEntity.getTicket());
                    // 设置授权有效时间
                    builder.setExpiresIn(1000 * 60 * 60L);
                    // 得到到客户端重定向地址

                    String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);

                    // 构建响应
                    final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
                    logger.info("服务端/responseCode内，返回的回调路径：" + response.getLocationUri());
                    String responceUri = response.getLocationUri();
                    // 根据OAuthResponse返回ResponseEntity响应
                    HttpHeaders headers = new HttpHeaders();
                    try {
                        headers.setLocation(new URI(response.getLocationUri()));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    Long time = System.currentTimeMillis();
                    String API_KEY = "b1gtuVZRWVh0BdBX";

                    StringBuffer buf = new StringBuffer();
                    buf.append(mobile);
                    buf.append(userEmpowerEntity.getTicket());
                    buf.append(clientid);
                    buf.append(time);
                    buf.append(API_KEY);
                    String sign = new ExternalMD5(buf.toString()).asHex();
                    map.clear();
                    map.put("mobile", mobile);
                    map.put("ticket", userEmpowerEntity.getTicket());
                    map.put("clientid", clientid);
                    map.put("time", time);
                    logger.info("mobile:" + mobile);
                    logger.info("ticket:" + userEmpowerEntity.getTicket());
                    logger.info("clientid:" + clientid);
                    logger.info("time:" + time);
                    return R.ok().put("authorityMap", map).put("code", "0").put("sign", sign);
                }
            } else {
                return R.error().put("code", "500");
            }

        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        }
        return null;
    }
}
