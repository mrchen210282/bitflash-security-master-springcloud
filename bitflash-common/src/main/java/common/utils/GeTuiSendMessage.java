package common.utils;

import cn.bitflash.utils.R;
import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.IIGtPush;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;

/**
 * @author chen
 */
public class GeTuiSendMessage {

    private static String appId = "6Jb1vKDvQ3A7To85QEIiS9";
    private static String appKey = "zez0aThjO98I9sR4cc4mc2";
    private static String masterSecret = "ETJdbQMg2vAE5FO56B09e4";
    private static String host = "http://sdk.open.api.igexin.com/apiex.htm";


    //单发
    public static R sendSingleMessage(String text, String cid) throws Exception {

        IIGtPush push = new IGtPush(host, appKey, masterSecret);
        IBatch batch = push.getBatch();

        SingleMessage message = new SingleMessage();
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(text);
        // template.setTransmissionType(1); // 这个Type为int型，填写1则自动启动app
        template.setTransmissionType(0); // 这个Type为int型，填写1则自动启动app

        message.setData(template);
        message.setOffline(true);
        message.setOfflineExpireTime(1 * 1000 * 60 * 60 * 24 * 7);
        // 设置推送目标，填入appid和clientId
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(cid);
        batch.add(message, target);

        return R.ok(batch.submit().getResponse().toString());
    }
}
