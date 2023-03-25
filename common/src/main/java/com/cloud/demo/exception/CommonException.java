package com.cloud.demo.exception;

import com.cloud.demo.enums.CommonExceptionEnum;

/**
 * @Author weiwei
 * @Date 2022/4/22 下午11:09
 * @Version 1.0
 * @Desc 通用错误
 */
public class CommonException extends RuntimeException {
    private CommonExceptionEnum exception;

    public CommonException(CommonExceptionEnum exception) {
        super(exception.getMessage());
        this.exception = exception;
    }

    public CommonException() {
        super();
    }

    public CommonExceptionEnum getException() {
        return exception;
    }

    public void setException(CommonExceptionEnum exception) {
        this.exception = exception;
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(Throwable cause) {
        super(cause);
    }
}
