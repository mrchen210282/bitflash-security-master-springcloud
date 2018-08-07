package cn.bitflash.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class AESTokenUtil {

    //获取base64解密方法
    private static  Base64.Decoder decoder = Base64.getDecoder();

    //获取base64加密方法
    private static Base64.Encoder encoder =Base64.getEncoder();

    private static String charsetName="UTF-8";
    /**
     * 解密token的方法
     * @param time head里的时间戳参数
     * @param str1 用time作为密匙加密的token,再用base64加密AES加密后的数据（请求头里的token数据）
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getToken(String time, String str1) throws UnsupportedEncodingException {
        //time为密匙解密，取22位长度
        time = time+"bkc";
        //获取AES加密的token
        String token=new String(decoder.decode(str1), charsetName);
        //创建AES加密方式，向量为密匙替换0->8
        AES aes=new AES(time,time);
        //解密后的token
        token=aes.decode(token);
        return token;
    }

    //解密数据
    public static String getData(String token,String str1) throws UnsupportedEncodingException{
        String data=new String(decoder.decode(str1),charsetName);
        token=token.substring(0,16);
        AES aes=new AES(token,token);
        data=aes.decode(data);
        return data;
    }

    //加密token
    public static String setToken(String time,String str1) throws UnsupportedEncodingException {
        //time为密匙加密，取22位长度
        time = time+"bkc";
        AES aes=new AES(time,time);
        String token=aes.encode(str1);
        token=new String(encoder.encode(token.getBytes(charsetName)),charsetName);
        return token;
    }

    //加密数据
    public static String setData(String token,String str1) throws UnsupportedEncodingException{
        token=token.substring(0,16);
        AES aes=new AES(token,token);
        String data=aes.encode(str1);
        data=new String(encoder.encode(data.getBytes(charsetName)),charsetName);

        return data;
    }



}
