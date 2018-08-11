package cn.bitflash.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author wangjun
 * @version 2018年8月10日下午1:49:12
 *
 */
public class BigDecimalUtils {

	public static String DecimalFormat(BigDecimal big) {
		String avaliable = "";
		if (null != big) {
			DecimalFormat df = new DecimalFormat("#.000");
			Double availableAssets = Double.valueOf(big.toString());
			avaliable = df.format(availableAssets);
		}
		return avaliable;
	}
}
