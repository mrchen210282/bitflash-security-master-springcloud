package cn.bitflash.utils;

import java.io.UnsupportedEncodingException;

public class AESTokenUtil {
    /**
     * 解密token的方法
     *
     * @param time head里的时间戳参数
     * @param str1 用time作为密匙加密的token,再用base64加密AES加密后的数据（请求头里的token数据）
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getToken(String time, String str1) {
        //time为密匙解密，取22位长度
        time = time + "bkc";
        //解密后的token
        String token = AES.aesDecrypt(str1, time);
        return token;
    }

    //解密数据
    public static String getData(String token, String str1)  {
        String data = AES.aesDecrypt(str1, token);
        return data;
    }


    //加密token
    public static String setToken(String time, String str1) {
        //time为密匙加密，取22位长度
        time = time + "bkc";
        String token = AES.aesEncrypt(str1, time);
        return token;
    }

    //加密数据
    public static String setData(String token, String str1)  {
        String data =  AES.aesEncrypt(str1, token);
        return data;
    }


}
