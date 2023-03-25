package com.cloud.demo.handler;

import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.exception.CommonException;
import com.cloud.demo.vo.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author weiwei
 * @Date 2022/6/28 下午10:44
 * @Version 1.0
 * @Desc
 */
@Component
public class MyExtendAuthenticationEntryPointHandler extends OAuth2AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(MyExtendAuthenticationEntryPointHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Throwable cause = authException.getCause();

        Result result;
        if (cause instanceof UsernameNotFoundException) {
            System.out.println("用户为注册哈");
            result = Result.fail(CommonExceptionEnum.ACCOUNT_NOT_EXIST);
        } else if (cause instanceof CommonException) {
            System.out.println("通用错误哈哈哈");
            result = Result.fail(((CommonException) cause).getException());
        } else if(cause instanceof InvalidGrantException) {
            System.out.println("账号或密码错误");
            result = Result.fail(CommonExceptionEnum.PASSWORD_ERROR);
        } else {
            result = Result.fail(500, "未知错误，请联系管理员");
        }

        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.append(new ObjectMapper().writeValueAsString(result));
    }
}
