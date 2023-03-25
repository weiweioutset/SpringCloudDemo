package com.cloud.demo.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerErrorException;

import java.io.IOException;

/**
 * @Author weiwei
 * @Date 2022/6/28 下午10:53
 * @Version 1.0
 * @Desc
 */
@Component
public class MyExtendOAuth2ResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {

        // Try to extract a SpringSecurityException from the stacktrace
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        // 异常栈获取 OAuth2Exception 异常
        Exception ase = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);
        if (ase != null) {
            return handleOAuth2Exception((OAuth2Exception) ase);
        }

        // 否则服务器内部错误
        return handleOAuth2Exception(new MyExtendOAuth2Exception(e.getMessage(), e));

    }

    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) throws IOException {
        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");

        // 使用自定义扩展OAuth2Exception
        e = new MyExtendOAuth2Exception(e.getMessage(), e);
        ResponseEntity<OAuth2Exception> response = new ResponseEntity<OAuth2Exception>(e, headers, HttpStatus.OK);
        return response;
    }


}

