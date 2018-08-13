package cn.bitflash.utils;

import java.util.List;

/**
 * @author wangjun
 */
public class Common {

	// 卖出
	public final static String STATE_SELL = "1";

	// 购买
	public final static String STATE_PURCHASE = "2";

	// 撤消
	public final static String STATE_CANCEL = "3";

	// 已付款
	public final static String STATE_PAY = "4";

	// 已锁定
	public final static String STATE_LOCK = "5";

	// 待确认
	public final static String STATE_CONFIRM = "6";

	// 已完成
	public final static String STATE_FINISH = "7";

	// 最小价格
	public final static String MIN_PRICE = "0.325";

	// 最小价格
	public final static Double MULTIPLE = 100d;

	// 超时时间(毫秒)
	public final static String OUT_TIME = "outTime";

	// 连接地址
	public final static String ADDRESS = "address";

	// vip等级0
	public final static String VIP_LEVEL_0 = "0";

	// vip等级1
	public final static String VIP_LEVEL_1 = "1";
	// vip等级2
	public final static String VIP_LEVEL_2 = "2";
	// vip等级3
	public final static String VIP_LEVEL_3 = "3";
	// vip等级4
	public final static String VIP_LEVEL_4 = "4";
	// vip等级5
	public final static String VIP_LEVEL_5 = "5";

	// 下载图片服务器地址
	public final static String PICTURE_URL = "picture_url";

	// redis缓存订单号key
	public final static String ADD_LOCK = "userTrade_";

	// redis统计锁定订单数量
	public final static String COUNT_LOCK = "countLock_";

	// 发送信息标题key值
	public final static String MSG_TITLE = "title_msg";

	// 发送信息标题内容值
	public final static String MSG_TEXT = "text_msg";

	public final static double poundage = 0.025;

	public final static String SHOW_DATE = "show_date";

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

}
