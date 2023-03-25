package com.cloud.demo.handler;

import com.alibaba.fastjson.JSONObject;
import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.vo.Result;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @Author weiwei
 * @Date 2022/5/15 下午9:29
 * @Version 1.0
 * @Desc 自定义返回结果：没有登录或token过期时
 */
@Component
public class AuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Result result = new Result(CommonExceptionEnum.NO_LOGIN_OR_TOKEN_INVALID.getCode(),CommonExceptionEnum.NO_LOGIN_OR_TOKEN_INVALID.getMessage());
        String body = JSONObject.toJSONString(result);
        DataBuffer buffer =  response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
