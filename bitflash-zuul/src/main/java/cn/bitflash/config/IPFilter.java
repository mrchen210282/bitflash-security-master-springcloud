package cn.bitflash.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class IPFilter extends ZuulFilter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // filterType：过滤器的类型，它决定过滤器在请求的哪个生命周期中执行。
    // pre：可以在请求被路由之前调用
    // route：在路由请求时候被调用
    // post：在route和error过滤器之后被调用
    // error：处理请求时发生错误时被调用
    @Override
    public String filterType() {
        return "pre";
    }

    // filterOrder：过滤器的执行顺序。
    // 当请求在一个阶段中存在多个过滤器时，需要根据该方法返回的值来依次执行。
    @Override
    public int filterOrder() {
        return 1;
    }

    // shouldFilter：判断该过滤器是否需要被执行。
    // true表示该过滤器对所有请求都会生效。
    // 实际运用中我们可以利用该函数来指定过滤器的有效范围
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String ipAddr=this.getIpAddr(request);
        List<String> ips=new ArrayList<String>();
        //添加黑名单地址
        ips.add("");
        if(ips.contains(ipAddr)){
            ctx.setResponseStatusCode(401);
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody("IpAddr is forbidden!");
        }
        return null;
    }
    /**
     * 获取Ip地址
     * @param request
     * @return
     */
    public  String getIpAddr(HttpServletRequest request){

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
