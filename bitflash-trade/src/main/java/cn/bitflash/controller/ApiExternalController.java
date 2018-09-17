package cn.bitflash.controller;

import cn.bitflash.service.GameAccountHistoryService;
import cn.bitflash.service.UserAccountService;
import cn.bitflash.trade.GameAccountHistoryEntity;
import cn.bitflash.trade.UserAccountEntity;
import cn.bitflash.utils.Common;
import cn.bitflash.utils.R;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chen
 */
@Controller
@RequestMapping("/api/external")
public class ApiExternalController {

    private Logger logger = LoggerFactory.getLogger(ApiExternalController.class);

//	@Autowired
//	private UserAccountGameService userAccountGameService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private GameAccountHistoryService gameAccountHistoryService;

    /**
     * @param
     * @return count 贝壳数量
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    // @OtherLogin
    @GetMapping("getBKCNum")
    public R getBKCNum(HttpServletRequest request) throws UnsupportedEncodingException {
        String uid = request.getParameter("uid");
        String time = request.getParameter("time");
        String sign = request.getParameter("sign");
        String apiKey = "b1gtuVZRWVh0BdBX";

        List<String> inParam = new ArrayList<>();
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
//				//薛总专用测试 start
//				if("DF3223E855254FB08B4B8A92EA84C230".equals(uid)) {
//
//					if (null != accountEntity) {
//						String count = accountEntity.getAvailableAssets().toString();
//
////					Integer intAvailableAssets = new Integer(count.toString());
//
//						Long timeVal = System.currentTimeMillis();
//						List<Object> outParam = new ArrayList<Object>();
//						outParam.add(count);
//						outParam.add(timeVal.toString());
//						outParam.add(apiKey);
//						String returnSign = Common.returnMD5(outParam);
//
//						logger.info("availableAssets:" + count);
//
//						return R.ok().put("availableAssets", count.toString()).put("code", 1).put("time", timeVal.toString()).put("sign", returnSign);
//					} else {
//						return R.error().put("code", "500");
//					}
//				}
                //end

                if (null != accountEntity) {
                    String count = accountEntity.getAvailableAssets().toString();
                    Long timeVal = System.currentTimeMillis();
                    List<String> outParam = new ArrayList<>();
                    outParam.add(count);
                    outParam.add(timeVal.toString());
                    outParam.add(apiKey);
                    String returnSign = Common.returnMD5(outParam);

                    logger.info("availableAssets:" + count);

                    return R.ok().put("availableAssets", count).put("code", "1").put("time", timeVal.toString()).put("sign", returnSign);
                } else {
                    return R.error().put("code", "500");
                }
            }
        }

        return R.error().put("code", "500");

    }

    /**
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    // @OtherLogin
    @GetMapping("changeBKCNum")
    public R changeBKCNum(HttpServletRequest request) throws UnsupportedEncodingException {
        logger.info("---------------changeBKCNum----------------");
        String uid = request.getParameter("uid");
        String flag = request.getParameter("flag");
        String bkcNum = request.getParameter("bkcNum");
        String time = request.getParameter("time");
        String sign = request.getParameter("sign");
        String apiKey = "b1gtuVZRWVh0BdBX";

        List<String> inParam = new ArrayList<>();
        inParam.add(uid);
        inParam.add(flag);
        inParam.add(bkcNum);
        inParam.add(time);
        inParam.add(apiKey);

        String mySign = Common.returnMD5(inParam);

        logger.info("uid:" + uid);
        logger.info("flag:" + flag);
        logger.info("bkcNum:" + bkcNum);
        logger.info("time:" + time);
        logger.info("sign:" + sign);

        if (sign.equals(mySign)) {
            if (StringUtils.isNotBlank(uid) && StringUtils.isNotBlank(bkcNum) && StringUtils.isNotBlank(flag)) {

                try {
                    if (Integer.valueOf(bkcNum) > 0) {
                        // 解密uid
                        UserAccountEntity accountEntity = userAccountService.selectById(uid);

                        if (null != accountEntity) {
                            BigDecimal regulateIncome = new BigDecimal(bkcNum);
                            //添加游戏记录
                            GameAccountHistoryEntity gameAccountHistoryEntity = new GameAccountHistoryEntity();
                            if (flag.equals("0")) {
                                // 加法
                                accountEntity.setRegulateIncome(accountEntity.getRegulateIncome().add(regulateIncome));
                            } else {
                                // 减法
                                if (accountEntity.getAvailableAssets().compareTo(regulateIncome) <= 0) {
                                    return R.error().put("code", "500");
                                } else {
                                    accountEntity.setRegulateIncome(accountEntity.getRegulateIncome().subtract(regulateIncome));
                                }
                            }
                            accountEntity.setAvailableAssets(accountEntity.getRegulateIncome().add(accountEntity.getRegulateRelease()));
                            userAccountService.updateById(accountEntity);

                            //添加游戏贝壳数
                            gameAccountHistoryEntity.setFlag(flag);
                            gameAccountHistoryEntity.setUid(accountEntity.getUid());
                            gameAccountHistoryEntity.setCreateTime(new Date());
                            gameAccountHistoryEntity.setQuantity(regulateIncome);
                            gameAccountHistoryService.insert(gameAccountHistoryEntity);

                            logger.info("availableAssets:" + accountEntity.getAvailableAssets().toString());
                            Long timeVal = System.currentTimeMillis();
                            List<String> outParam = new ArrayList<>();
                            outParam.add(accountEntity.getAvailableAssets().toString());
                            outParam.add(timeVal.toString());
                            outParam.add(apiKey);
                            String returnSign = Common.returnMD5(outParam);

                            return R.ok().put("code", "1").put("availableAssets", accountEntity.getAvailableAssets().toString()).put("time", timeVal.toString()).put("sign", returnSign);
                        } else {
                            logger.info("查询不到用户信息！");
                        }
                    } else {
                        logger.info("贝壳数量为:" + bkcNum);
                    }
                } catch (Exception ex) {
                    logger.info("贝壳数量为:" + bkcNum + ",转换为int值失败！");
                    ex.printStackTrace();
                    return R.error().put("code", "500");
                }
            } else {
                logger.info("密钥错误！");
            }
        }
        return R.error().put("code", "500");
    }
}