package com.xxxx.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * 2次MD5加密
 */
@Component
public class MD5Util {
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    /**
     * 将input框输入的明文密码根据公有盐转化为MD5加密密码
     *
     * @param inputPass
     * @return java.lang.String
     * @author LC
     **/
    public static String inputPassToFormPass(String inputPass) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * 第将根据公有盐转化的MD5加密密码  再根据私有盐进行第二次加密，同时应该在DB中存储私有盐
     *
     * @param formPass
     * @param salt
     * @return java.lang.String
     **/
    public static String formPassToDBPass(String backendPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + backendPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt) {
        return formPassToDBPass(inputPassToFormPass(inputPass), salt);
    }

    public static void main(String[] args) {
        // d3b1294a61a07da9b49b6e22b2cbd7f9
        System.out.println(inputPassToFormPass("123456"));
        System.out.println(formPassToDBPass("d3b1294a61a07da9b49b6e22b2cbd7f9","1a2b3c4d"));
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
    }
}
