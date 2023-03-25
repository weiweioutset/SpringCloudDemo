package com.cloud.demo.handler;

import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.exception.CommonException;
import com.cloud.demo.vo.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author weiwei
 * @Date 2022/5/11 下午9:25
 * @Version 1.0
 * @Desc 网关层全局异常处理
 */
@Order(-1)
@Component
public class GlobalErrorExceptionHandler implements ErrorWebExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorExceptionHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        LOGGER.error("GateException", ex);
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        // JSON格式返回
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        // 构建错误信息
        Result<String> result = Result.fail(CommonExceptionEnum.UNKNOWN_ERROR);

        if (ex.getCause() instanceof TimeoutException) {
            // 网关超时
            result = Result.fail(CommonExceptionEnum.TIMEOUT_EXCEPTION);
        } else if (ex instanceof CommonException) {
            // 自定义错误
            result = Result.fail((CommonException)ex);
        } else if (ex instanceof NotFoundException) {
            // 下游服务不可用
            NotFoundException exception = (NotFoundException) ex;
            int statusCode = exception.getStatus().value();
            if (statusCode == 503) {
                LOGGER.info("下游服务不可用," + exception.getReason());
                result = Result.fail(CommonExceptionEnum.SERVICE_UNAVAILABLE);
            }
        } else if (ex instanceof ResponseStatusException) {
            // 接口404
            HttpStatus httpStatus = ((ResponseStatusException) ex).getStatus();
            response.setStatusCode(httpStatus);
            int code = httpStatus.value();
            if (code == 404) {
                result = Result.fail(CommonExceptionEnum.RESOURCE_NOT_FOUND);
            }
        }
        Result<String> finalResult = result;
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(finalResult));
            }catch (JsonProcessingException e) {
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
