package com.cloud.demo.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * @Author weiwei
 * @Date 2022/5/7 下午10:41
 * @Version 1.0
 * @Desc RSA非对称加密算法工具类
 */
public class RSAUtils {
    public static final String ENCRYPTION_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    // 公钥
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8LyWXPMVzeF+JYFrWp3ewfViSyY/9buQqtsU9WRfEjRUJtPrnCjTBGOslRCQh0KL66vS7kRLufo/NaQLMkyLqUrmm3XWLtmJDkZVSf4hqi4fRYEvLG/QI/4WyboMBXu/UA4tGZbxRne7ZoQG1G2a8WLQLcpLDdDOPNkv+d8MKawIDAQAB";
    // 私钥
    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALwvJZc8xXN4X4lgWtand7B9WJLJj/1u5Cq2xT1ZF8SNFQm0+ucKNMEY6yVEJCHQovrq9LuREu5+j81pAsyTIupSuabddYu2YkORlVJ/iGqLh9FgS8sb9Aj/hbJugwFe79QDi0ZlvFGd7tmhAbUbZrxYtAtyksN0M482S/53wwprAgMBAAECgYAw5nd4M1yDwvXgdt9kBCSAAjJwAlyeWKCF0PS0GmCovJiI6zR/sRFAVD9WcbyJ5PNVDJUH4Izw2IZ5tB38FwftNbDKU5a+H5TfXH2Y98nRQ6OC/0YcicOebuNhcuWyCe1YXn4e+ibm1wqoamELZcXib4kxEcIOL2Rw9UyR4GagMQJBAOvIv68Bbld5bYWpIakOOj6ef19JkBfseSmrcFX2BWTQ8NlkxTB0Od3D5J8gGrcYIhE7czgyywNYoH8WpTgrhVcCQQDMUZ7e05L6gkWJIJJJT+6eEvr0g+blmhBLMo2GFsGY5qmA0bsEq0LN0x3RyTlccN+G0qEFzhkRuK8DP7QHZMMNAkEAkLNWz/Q034Ip8MlnWvTcUem7iaL/x27cvPH1swuVK9X+uavaSG5LxQmfmPh+7Lbm0WSGxYqGhLszZEtlgh9TaQJANbHRhY+mlaz8py/nIdsew24Lg4zSdk6Jr6lmZfG/CPa9Xvw/INf9I2gy8vnl2DRVmfHqnULvdafKXpk0L6k8VQJBAOFhIW6aL/wY3kHvI/yY77Sq3b7EpgYape1Uia9H6kHsZWphfrZDRi4W6YLbVPbHVXjc3JHLVTP4LhBmWqTiAwA=";

    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;


    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String publicKeyEncrypt(String str) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(PUBLIC_KEY);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(ENCRYPTION_ALGORITHM).
                generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        //当长度过长的时候，需要分割后加密 117个字节
        byte[] resultBytes = getMaxResultEncrypt(str, cipher);

        return Base64.encodeBase64String(resultBytes);
    }

    /**
     * RSA私钥加密
     *
     * @param str       加密字符串
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String privateKeyEncrypt(String str) throws Exception {
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(PRIVATE_KEY);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(ENCRYPTION_ALGORITHM)
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, priKey);

        //当长度过长的时候，需要分割后加密 117个字节
        byte[] resultBytes = getMaxResultEncrypt(str, cipher);

        return Base64.encodeBase64String(resultBytes);
    }

    /**
     * RSA公钥解密
     *
     * @param str        加密字符串
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String publicKeyDecrypt(String str) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(PUBLIC_KEY);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(ENCRYPTION_ALGORITHM).
                generatePublic(new X509EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        //当长度过长的时候，需要分割后解密 128个字节
        return new String(getMaxResultDecrypt(str, cipher));
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String privateKeyDecrypt(String str) throws Exception {
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(PRIVATE_KEY);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(ENCRYPTION_ALGORITHM)
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        //当长度过长的时候，需要分割后解密 128个字节
        return new String(getMaxResultDecrypt(str, cipher));
    }

    /**
     * 对信息生成数字签名（用私钥）
     */
    public static String sign(byte[] data) throws Exception {
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(PRIVATE_KEY);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(ENCRYPTION_ALGORITHM)
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);

        return Base64Util.encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名（用公钥）
     */
    public static boolean verify(byte[] data, String sign)
            throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(PUBLIC_KEY);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(ENCRYPTION_ALGORITHM).
                generatePublic(new X509EncodedKeySpec(decoded));

        // 取公钥匙对象
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(Base64Util.decryptBASE64(sign));
    }

    private static byte[] getMaxResultEncrypt(String str, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        byte[] inputArray = str.getBytes();
        return getMaxResult(inputArray, MAX_ENCRYPT_BLOCK, cipher);
    }

    private static byte[] getMaxResultDecrypt(String str, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] inputArray = Base64.decodeBase64(str.getBytes("UTF-8"));
        return getMaxResult(inputArray, MAX_DECRYPT_BLOCK, cipher);
    }

    private static byte[] getMaxResult(byte[] inputArray, int maxBlock, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        int inputLength = inputArray.length;
        // 标识
        int offSet = 0;
        byte[] resultBytes = {};
        byte[] cache = {};
        while (inputLength - offSet > 0) {
            if (inputLength - offSet > maxBlock) {
                cache = cipher.doFinal(inputArray, offSet, maxBlock);
                offSet += maxBlock;
            } else {
                cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        return resultBytes;
    }

    /**
     * 生成KeyPair
     * @return
     */
    public static KeyPair generateKeyPair() {
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "weiwei145905".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "weiwei145905".toCharArray());
    }

    public static void main(String[] args) throws Exception {
        String key = publicKeyEncrypt("weiwei145905");
        System.out.println(key);
        System.out.println(URLEncoder.encode(key, "UTF-8"));

        System.out.println(privateKeyEncrypt("weiwei145905"));

        String test = privateKeyDecrypt(key);
        System.out.println(test);
    }
}
