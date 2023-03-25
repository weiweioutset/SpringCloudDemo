package com.cloud.demo.controller;

import com.cloud.demo.utils.KeyPairUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * @Author weiwei
 * @Date 2022/5/15 下午8:40
 * @Version 1.0
 * @Desc
 */
@RestController
public class KeyPairController {

    @GetMapping("/rsa/publicKey")
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) KeyPairUtils.generateKeyPair().getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }
}
