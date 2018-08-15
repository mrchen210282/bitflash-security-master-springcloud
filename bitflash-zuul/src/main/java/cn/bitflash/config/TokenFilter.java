package cn.bitflash.config;

import cn.bitflash.utils.AESTokenUtil;
import cn.bitflash.utils.Common;
import cn.bitflash.utils.R;
import cn.bitflash.utils.RedisUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * zuul拦截器
 *
 * @author eric
 */
public class TokenFilter extends ZuulFilter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisUtils redisUtils;

    public static final String TIME = "time";
    public static final String TOKEN = "token";
    public static final String MOBILE = "mobile";
    public static final Long DIFFERENCE = 1000 * 10L;

    /**
     * filterType：过滤器的类型，它决定过滤器在请求的哪个生命周期中执行。
     * pre：可以在请求被路由之前调用
     * route：在路由请求时候被调用
     * post：在route和error过滤器之后被调用
     * error：处理请求时发生错误时被调用
     *
     * @return pre
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * filterOrder：过滤器的执行顺序
     * 当请求在一个阶段中存在多个过滤器时，需要根据该方法返回的值来依次执行。
     * 数字越小，执行过程越往前
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 如果请求地址中标包含登录接口则不执行该过滤器
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String url = request.getRequestURI();
        if (url.indexOf("bitflash-login/api/login/login") != -1) {
            return false;
        }
        if (url.indexOf("bitflash-login/api/reg") != -1) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String secretTime = request.getHeader(TIME);
        String secretToken = request.getHeader(TOKEN);
        String mobile = request.getHeader(MOBILE);
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(secretTime)) {
            secretTime = request.getParameter(TIME);
        }
        if (StringUtils.isBlank(secretToken)) {
            secretToken = request.getParameter(TOKEN);
        }
        //token为空
        if (StringUtils.isBlank(secretTime) || StringUtils.isBlank(secretToken)) {
            this.errorMessage(ctx, "token值不能为空");
            return null;
        }
        try {
            HttpSession session = request.getSession();
            session.setAttribute(Common.MOBILE, mobile);
            String token = AESTokenUtil.getToken(secretTime, secretToken);
            session.setAttribute(TOKEN, token);
        } catch (Exception e) {
            this.errorMessage(ctx, "解密异常");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void errorMessage(RequestContext ctx, String mess) {
        HttpServletResponse response = ctx.getResponse();
        response.setCharacterEncoding("utf-8");  //设置字符集
        response.setContentType("text/html; charset=utf-8"); //设置相应格式
        response.setStatus(401);
        ctx.setSendZuulResponse(false); //不进行路由
        try {
            response.getWriter().write(R.error(mess).toString()); //响应体
        } catch (IOException e) {
            logger.error("response io异常");
            e.printStackTrace();
        }
        ctx.setResponse(response);
    }
}
