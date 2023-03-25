package com.cloud.demo.vo;

import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.exception.CommonException;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @Author weiwei
 * @Date 2022/4/22 下午11:01
 * @Version 1.0
 * @Desc
 */
public class Result<T> implements Serializable {
    /**
     * 结果码
     */
    private int code;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    public Result() {
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static Result success() {
        return new Result<>(200,"success");
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(200,"success");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail() {
        return fail(CommonExceptionEnum.SYSTEM_ERROR);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code,message);
    }

    public static <T> Result<T> fail(CommonExceptionEnum exception) {
        return new Result<>(exception.getCode(),exception.getMessage());
    }

    public static <T> Result<T> fail(CommonException exception) {
        CommonExceptionEnum exceptionEnum = exception.getException();
        return new Result<>(exceptionEnum.getCode(),exceptionEnum.getMessage());
    }

    public boolean isSuccess() {
        return HttpStatus.OK.value() == code;
    }
}
