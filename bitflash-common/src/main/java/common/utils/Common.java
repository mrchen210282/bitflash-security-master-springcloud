package common.utils;

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

    // 下载图片服务器地址
    public final static String PICTURE_URL = "picture_url";

    // 赠送比例系数
    public final static String GIVE_RATIO = "give_ratio";


    // 升级vip的贝壳数量
    public final static String VIP_CONDITION = "vip_conditions";

    // redis缓存订单号key
    public final static String ADD_LOCK = "userTrade_";

    // redis统计锁定订单数量
    public final static String COUNT_LOCK = "countLock_";

    // 发送信息标题key值
    public final static String MSG_TITLE = "title_msg";

    // 发送信息标题内容值
    public final static String MSG_TEXT = "text_msg";

}
