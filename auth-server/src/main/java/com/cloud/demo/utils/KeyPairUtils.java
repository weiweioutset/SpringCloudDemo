package com.cloud.demo.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.io.File;
import java.security.KeyPair;

/**
 * @Author weiwei
 * @Date 2022/5/16 下午9:44
 * @Version 1.0
 * @Desc
 */
public class KeyPairUtils {
    /**
     * 生成KeyPair
     * @return
     */
    public static KeyPair generateKeyPair() {
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "weiwei145905".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "weiwei145905".toCharArray());
    }

    public static void main(String[] args) throws Exception{
        Resource resource = new ClassPathResource("bootstrap.yml");
        File file = resource.getFile();
        if (file.exists()) {
            System.out.println(file.getAbsolutePath());
        } else {
            System.out.println("not exists");
        }
    }
}
