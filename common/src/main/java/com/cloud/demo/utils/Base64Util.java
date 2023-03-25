package com.cloud.demo.utils;

import org.springframework.util.Base64Utils;

/**
 * @Author weiwei
 * @Date 2022/5/7 下午10:39
 * @Version 1.0
 * @Desc
 */
public class Base64Util {
    /**
     * Base64加密
     */
    public static String encryptBASE64(byte[] key) {
        return Base64Utils.encodeToString(key);
    }

    /**
     * Base64解密
     */
    public static byte[] decryptBASE64(String key) {
        return Base64Utils.decodeFromString(key);
    }
}
