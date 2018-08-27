package cn.bitflash.utils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * @author wangjun
 */
public class Common {

	// 卖出
	public final static String STATE_SELL = "1";

	// 撤消
	public final static String STATE_CANCEL = "3";

	// 已付款
	public final static String STATE_PAY = "4";

	// 已锁定
	public final static String STATE_LOCK = "5";

	// 待确认
	public final static String STATE_CONFIRM = "6";

	// 申拆中
	public final static String STATE_COMPLAINT = "9";

	//------------------求购------------------------

	// 已撤销
	public final static String STATE_BUY_CANCELFIISH = "0";

	// 可撤销
	public final static String STATE_BUY_CANCEL = "1";

	// 待付款
	public final static String STATE_BUY_PAYMONEY = "2";

	// 代收款
	public final static String STATE_BUY_ACCMONEY = "3";

	// 待确认
	public final static String STATE_BUY_PAYCOIN = "4";

	// 代收币
	public final static String STATE_BUY_ACCCOIN = "5";

	// 完成
	public final static String STATE_BUY_FINISH = "6";

	// 申诉中
	public final static String STATE_BUY_APPEAL = "9";

	//------------------finish----------------------

	//----------------code值---------------------

	public final static String SUCCESS = "交易成功";
	public final static String FAIL = "交易失败";
	public final static String TRADEMISS = "交易不存在";
	public final static String TRADECANCEL = "交易已取消";
	public final static String USERMISS = "用户不存在";

	// 最小价格
	public final static String MIN_PRICE = "0.33";

	public final static Double MIN_NUMBER = 100d;

	// 最小价格
	public final static Double MULTIPLE = 10d;

	// 超时时间(毫秒)
	public final static String OUT_TIME = "outTime";

	// 连接地址
	public final static String ADDRESS = "address";

	// vip等级0
	public final static String VIP_LEVEL_0 = "0";

	// 下载图片服务器地址
	public final static String PICTURE_URL = "picture_url";

	// redis缓存订单号key
	public final static String ADD_LOCK = "userTrade_";

	// redis缓存订单号key
	public final static String ADD_LOCKNUM = "userBuy_";

	// redis统计锁定订单数量
	public final static String COUNT_LOCK = "countLock_";

	// 发送信息标题内容值
	public final static String MSG_TEXT = "text_msg";

	public final static double poundage = 0.025;

	public final static String SHOW_DATE = "show_date";

	public final static String TOKEN = "token";
	public final static String MOBILE = "mobile";

	//交易手续费
	public final static String TRADE_CONFIG_ID = "1";

	//申拆状态:未处理0
	public final static String COMPLAINT_NO = "0";

	//申拆状态:已处理1
	public final static String COMPLAINT_YES = "1";

	public static String returnMD5(List<String> list) {
		if (list.size() > 0) {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				buf.append(list.get(i));
			}
			String returnSign = new ExternalMD5(buf.toString()).asHex();
			return returnSign;
		} else {
			return null;
		}
	}

	/**
	 * 生成8位随机数
	 * @return
	 */
	public static int randomUtil(){
		Random random = new Random();
		return 10000000 + random.nextInt(90000000);
	}

	/**
	 *
	 * 保留两个小数，并不进行四舍五入
	 * 100.00 > 100.00
	 * 100.10 > 100.10
	 * 100.01 > 100.01
	 * @param 要格式化的值
	 * @return
	 */
	public static String decimalFormat(double d) {
		if(d > 0) {
			DecimalFormat df = new DecimalFormat("######0.00");
			String str = df.format(d);
			return str;
		} else {
			return null;
		}

	}
}
