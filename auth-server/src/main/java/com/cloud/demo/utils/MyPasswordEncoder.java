package com.cloud.demo.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;

/**
 * @Author weiwei
 * @Date 2022/5/9 下午9:11
 * @Version 1.0
 * @Desc 自定义密码加密工具
 */
@Component
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        String strPassword = (String) rawPassword;
        // 需要先将前端加密的密码解密
        try {
            strPassword = RSAUtils.privateKeyDecrypt(strPassword);
        } catch (Exception ignore) {
            strPassword = "";
        }
        return SecurityUtils.RSAEncrypt(strPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 需要先将前端加密的密码解密
        String strPassword;
        try {
            strPassword = URLDecoder.decode((String) rawPassword, "UTF-8");
            strPassword = RSAUtils.privateKeyDecrypt(strPassword);
        } catch (Exception ignore) {
            strPassword = "";
        }
        String encodeNew = SecurityUtils.RSAEncrypt(strPassword);
        return encodedPassword.equals(encodeNew);
    }

    public static void main(String[] args) {
        String strPassword = "jlarP+aoPzsl2fPr6cB/nYFUysFW0D8wNZZ5ckVrXgKZo2/mcQZBmgZ6/VS5VVYv5Sauk2zy+" +
                "tyYag8mrJJcWcspA+KTn2mRKIJcSntopqhtMXIzU3X6GXUu6wH8h3NNikE5DWXBQwmlVpm3W/3pJu0FayB9x3GddQ/PUNQh4tA=";
        // 需要先将前端加密的密码解密
        try {
            strPassword = RSAUtils.privateKeyDecrypt(strPassword);
        } catch (Exception ignore) {
            strPassword = "";
        }
        String result = SecurityUtils.RSAEncrypt(strPassword);
        System.out.println(strPassword+"\n" + result);
    }
}
