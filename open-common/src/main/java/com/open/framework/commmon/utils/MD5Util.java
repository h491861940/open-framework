package com.open.framework.commmon.utils;

import java.security.MessageDigest;

public class MD5Util {

    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
                                                "e", "f" };

    private final static String   salt      = "heesWuhan";
    private final static String   algorithm = "MD5";

    /**
     * 给密码加密
     * 
     * @param password 输入的字符串密码
     * @return 加固定盐的MD5密码，存入数据库
     */
    public static String encode(String password) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            // 加盐
            String rawPass = (password == null ? "" : password) + "{" + salt + "}";
            // 加密后的字符串
            result = byteArrayToHexString(md.digest(rawPass.getBytes("utf-8")));
        } catch (Exception ex) {
        }
        return result;
    }

    /**
     * 校验密码
     * 
     * @param encPass 存在数据库的MD5密码
     * @param password 输入的字符串密码
     * @return
     */
    public static boolean isPasswordValid(String encPass, String password) {
        return encPass.equals(encode(password));
    }

    /**
     * 转换字节数组为16进制字串
     * 
     * @param b 字节数组
     * @return 16进制字串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0){ n = 256 + n;}
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

}
