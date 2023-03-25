package com.cloud.demo.handler;

import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.exception.CommonException;
import com.cloud.demo.vo.Result;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author weiwei
 * @Date 2022/6/28 下午10:55
 * @Version 1.0
 * @Desc
 */
@JsonSerialize(using = MyExtendOAuth2ExceptionSerializer.class)
public class MyExtendOAuth2Exception extends OAuth2Exception {
    private CommonExceptionEnum error;

    public MyExtendOAuth2Exception(Throwable t) {
        this("未知错误",t);
    }

    public MyExtendOAuth2Exception(String msg, Throwable t) {
        super(msg, t);

        if(t instanceof InvalidGrantException) {
            System.out.println("账号或密码错误");
            error = CommonExceptionEnum.PASSWORD_ERROR;
            return;
        }

        if (t.getCause() == null) {
            error = CommonExceptionEnum.UNKNOWN_ERROR;
            return;
        }

        Throwable cause = t.getCause().getCause();
        if (cause instanceof CommonException) {
            System.out.println("通用错误");
            error = ((CommonException) cause).getException();
        } else if(cause instanceof InvalidGrantException) {
            System.out.println("账号或密码错误");
            error = CommonExceptionEnum.PASSWORD_ERROR;
        } else {
            error = CommonExceptionEnum.UNKNOWN_ERROR;
        }
    }

    public CommonExceptionEnum getError() {
        return error;
    }

    public void setError(CommonExceptionEnum error) {
        this.error = error;
    }
}

