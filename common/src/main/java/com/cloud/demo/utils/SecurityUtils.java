package com.cloud.demo.utils;

import com.cloud.demo.constant.BaseConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @Author weiwei
 * @Date 2022/5/7 下午10:41
 * @Version 1.0
 * @Desc
 */
public class SecurityUtils {
    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
    private static final String DES_ENCRYPT_KEY = "A#B2C3D4E5F6070*CG=54/87";

    /**
     * RSA加密(私钥)
     * @param content
     * @return
     */
    public static String RSAEncrypt(String content) {
        if (StringUtil.isEmpty(content)) {
            return null;
        }
        try {
            return RSAUtils.privateKeyEncrypt(content);
        } catch (Exception e) {
            logger.error("RSA加密失败", e);
        }
        return content;
    }

    /**
     * RSA解密(公钥)
     * @param content
     * @return
     */
    public static String RSADecrypt(String content) {
        if (StringUtil.isEmpty(content)) {
            return null;
        }
        try {
            return RSAUtils.publicKeyDecrypt(content);
        } catch (Exception e) {
            logger.error("RSA解密失败", e);
        }
        return content;
    }

    /**
     * DES对称加密
     * @param content 原文
     * @return 密文
     */
    public static String DESEncrypt(String content) {
        if (StringUtil.isEmpty(content)) {
            return null;
        }
        try {
            return DESUtil.encrypt(content, DES_ENCRYPT_KEY);
        } catch (Exception e) {
            logger.error("DES加密错误", e);
        }
        return content;
    }

    /**
     * DES对称加密
     * @param content 原文
     * @return 密文
     */
    public static String DESDecrypt(String content) {
        if (StringUtil.isEmpty(content)) {
            return null;
        }
        try {
            return DESUtil.decrypt(content, DES_ENCRYPT_KEY);
        } catch (Exception e) {
            logger.info("DES解密失败");
        }
        return null;
    }

    /**
     * 检验密码是否为强密码
     * @param password
     * @return
     */
    public static boolean isStrongPassword(String password) {
        if (StringUtil.isEmpty(password)) {
            return false;
        }
        // 密码不能小于6位
        if (password.length() < BaseConstant.MINIMUM_PASSWORD) {
            return false;
        }
        // 密码不能是纯数字
        if (StringUtil.isNumeric(password)) {
            return false;
        }
        return true;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) throws Exception{
        String pwd = "weiwei145905";
        String p = RSAEncrypt(pwd);
        String w = RSADecrypt(p);

        System.out.println("p = " + p + "\nw = " + w);
    }
}
