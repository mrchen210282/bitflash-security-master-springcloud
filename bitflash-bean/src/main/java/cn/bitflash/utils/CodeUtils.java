package cn.bitflash.utils;

/**
 * @author soso
 * @date 2018年5月22日 下午10:23:32
 */

public class CodeUtils {
    public static String genInvitationCode() {
        // 字符源，可以根据需要删减
        String generateSource = "23456789abcdefghgklmnpqrstuvwxyz";// 去掉1和i ，0和o
        String rtnStr = "";
        for (int i = 0; i < 8; i++) {
            // 循环随机获得当次字符，并移走选出的字符
            String nowStr = String
                    .valueOf(generateSource.charAt((int) Math.floor(Math.random() * generateSource.length())));
            rtnStr += nowStr;
            generateSource = generateSource.replaceAll(nowStr, "" );
        }

        return rtnStr.toUpperCase();

    }

    public static void main(String[] arg) {
        for (int i = 0; i < 20; i++) {
            System.out.println("lft   " + genInvitationCode() + "***** rgt   " + genInvitationCode());
        }
    }

}
