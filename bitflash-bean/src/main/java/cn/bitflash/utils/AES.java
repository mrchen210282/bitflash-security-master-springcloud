package cn.bitflash.utils;

import cn.bitflash.exception.RRException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static javax.crypto.Cipher.ENCRYPT_MODE;


public class AES {

    /**
     * 算法名称
     */
    public static final String NAME = "AES";

    /**
     * 加密模式：CBC；数据块：128；填充：PKCS5Padding
     */
    public final String MODE = "AES/CBC/PKCS5Padding";


    /**
     * 加密用的 KEY
     */
    private String key;

    /**
     * 向量，用于增加加密强度
     */
    private String ivParameter;

    /**
     * @param key         加密用的 KEY
     * @param ivParameter 偏移量
     */
    public AES(String key, String ivParameter) {
        if (key == null) {
            throw new RRException("KEY 不存在");
        }
        if (ivParameter == null) {
            throw new RRException("ivParameter 不存在");
        }

        this.key = key;
        this.ivParameter = ivParameter;
    }

    /**
     * 加密
     *
     * @param s 要加密的字符串
     * @return 加密后的字符串
     */
    public String encode(String s) {
        String result;
        try {

            Cipher cipher = Cipher.getInstance(MODE);
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), NAME), iv);
            byte[] bytes = cipher.doFinal(s.getBytes("UTF-8"));
            result = new BASE64Encoder().encode(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RRException("加密", e);
        }
        return result;
    }

    /**
     * 解密
     *
     * @param s 待解密的字符串
     * @return 解密后的字符串
     */
    public String decode(String s) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("ASCII"), NAME);
            Cipher cipher = Cipher.getInstance(MODE);
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            return new String(cipher.doFinal(new BASE64Decoder().decodeBuffer(s)), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RRException("解密", e);
        }
    }
}
