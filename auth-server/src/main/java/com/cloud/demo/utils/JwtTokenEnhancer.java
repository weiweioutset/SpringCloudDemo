package com.cloud.demo.utils;

import com.cloud.demo.vo.SecurityUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author weiwei
 * @Date 2022/5/15 下午6:59
 * @Version 1.0
 * @Desc JWT内容增强器
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Map<String, Object> info = new HashMap<>();
        //把用户ID设置到JWT中
        info.put("accountId", securityUser.getAccountId());
        info.put("userFullName", securityUser.getUserFullName());
        Object detailObject = authentication.getUserAuthentication().getDetails();
        if (detailObject instanceof LinkedHashMap) {
            LinkedHashMap details = (LinkedHashMap) detailObject;
            info.put("client_id", details.get("client_id"));
            info.put("grant_type", details.get("grant_type"));
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}