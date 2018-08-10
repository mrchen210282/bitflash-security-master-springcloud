package cn.bitflash.controller;

import cn.bitflash.feignController.LoginFeign;
import cn.bitflash.user.TokenEntity;
import cn.bitflash.utils.R;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chen
 */
@RestController
@RequestMapping("/api/external")
public class ApiExternalController {

    @Autowired
    private LoginFeign loginFeign;

    private Logger logger = LoggerFactory.getLogger(ApiExternalController.class);

    /**
     *
     * @param token
     * @return count 贝壳数量
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    // @OtherLogin
    @GetMapping("getBKCNum")
    @ApiOperation("第三方获取贝壳数量")
    public R getBKCNum(HttpServletRequest request) throws UnsupportedEncodingException {
        String uid = request.getParameter("uid");
        String time = request.getParameter("time");
        String sign =  request.getParameter("sign");
        String apiKey = "b1gtuVZRWVh0BdBX";

        List<String> inParam = new ArrayList<String>();
        inParam.add(uid);
        inParam.add(time);
        inParam.add(apiKey);

        String mySign = Common.returnMD5(inParam);


        logger.info("time:" + time);
        logger.info("uid:" + uid);
        if (sign.equals(mySign)) {
            if (StringUtils.isNotBlank(time) && StringUtils.isNotBlank(uid)) {
                // String uid = token.getUid();
                // 解密uid
                UserAccountEntity accountEntity = userAccountService.selectById(uid);
                if (null != accountEntity) {
                    String count = accountEntity.getAvailableAssets().toString();

                    Long timeVal = System.currentTimeMillis();
                    List<String> outParam = new ArrayList<String>();
                    outParam.add(count);
                    outParam.add(timeVal.toString());
                    outParam.add(apiKey);
                    String returnSign = Common.returnMD5(outParam);

                    return R.ok().put("availableAssets", count).put("code", 1).put("time", timeVal.toString()).put("sign", returnSign);
                } else {
                    return R.error().put("code", "500");
                }
            }
        }

        return R.error().put("code", "500");

    }

}
