package com.cloud.demo.handler;

import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.exception.CommonException;
import com.cloud.demo.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * @Author weiwei
 * @Date 2022/5/7 下午10:43
 * @Version 1.0
 * @Desc
 */
@RestControllerAdvice()
public class GlobalExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理 form data方式调用接口校验失败抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public Result<List<String>> bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> errors = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        Result<List<String>> result = new Result<>(CommonExceptionEnum.INVALID_PARAMS.getCode(), "参数错误");
        result.setData(errors);
        return result;
    }

    /**
     * 处理 json 请求体调用接口校验失败抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<List<String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> errors = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        Result<List<String>> result = new Result<>(CommonExceptionEnum.INVALID_PARAMS.getCode(), "请求数据错误");
        result.setData(errors);
        return result;
    }

    /**
     * 处理单个参数校验失败抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<List<String>> constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> errors = constraintViolations.stream()
                .map(o -> o.getMessage())
                .collect(Collectors.toList());
        Result<List<String>> result = new Result<>(CommonExceptionEnum.INVALID_PARAMS.getCode(), "参数错误");
        result.setData(errors);
        return result;
    }

    /**
     * 自定义异常
     * @param exception
     * @return
     */
    @ExceptionHandler(CommonException.class)
    public Result<String> exception(CommonException exception) {
        LOGGER.error(exception.getException().getMessage());
        return Result.fail(exception.getException());
    }

    /**
     * 请求超时
     * @param exception
     * @return
     */
    @ExceptionHandler(TimeoutException.class)
    public Result<String> exception(TimeoutException exception) {
        LOGGER.error(exception.getMessage());
        return Result.fail(CommonExceptionEnum.TIMEOUT_EXCEPTION);
    }

    @ExceptionHandler(Exception.class)
    public Result<String> exception(Exception exception) {
        LOGGER.error("未知错误", exception);
        return Result.fail(CommonExceptionEnum.UNKNOWN_ERROR);
    }

}
