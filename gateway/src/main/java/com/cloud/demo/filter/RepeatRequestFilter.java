package com.cloud.demo.filter;

import com.cloud.demo.constant.RedisKeyConstant;
import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.utils.IPUtils;
import com.cloud.demo.utils.RedisUtils;
import com.cloud.demo.utils.TimeUtil;
import com.cloud.demo.vo.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @Author weiwei
 * @Date 2022/7/13 上午12:10
 * @Version 1.0
 * @Desc 请求过滤器，限制请求频率
 */
@Component
public class RepeatRequestFilter implements GlobalFilter, Ordered {
    private final Logger LOGGER = LoggerFactory.getLogger(RepeatRequestFilter.class);
    @Autowired
    private RedisUtils redisUtils;
    private static final String REPEAT_REQUEST_KEY = "REPEAT_REQUEST:";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 获取URL和IP地址
        String url = request.getURI().getPath();
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        String remoteAddressStr;
        if (remoteAddress == null) {
            remoteAddressStr = IPUtils.LOCAL_IP;
        } else {
            remoteAddressStr = remoteAddress.getHostString();
        }
        String ip = IPUtils.getIPAddress(exchange.getRequest().getHeaders(), remoteAddressStr);
        String key = url + ":" + ip;
        Object lastRequest = redisUtils.getValue(REPEAT_REQUEST_KEY + key);
        if (lastRequest == null) {
            // 如果是GET请求，限制500ms内只能发一次，如果是POST请求，限制为2s
            String methodValue = request.getMethodValue();
            long expireTime = RedisKeyConstant.POST_REQUEST_DURATION;
            if ("GET".equals(methodValue)) {
                expireTime = RedisKeyConstant.GET_REQUEST_DURATION;
            } else if ("/user/register".equals(url)) {
                // 对于账号注册，同一个IP10个小时内只能注册一个账号
                expireTime = RedisKeyConstant.REGISTER_REQUEST_DURATION;
            }
            long time = System.currentTimeMillis();
            redisUtils.setValue(REPEAT_REQUEST_KEY + key, time, expireTime, TimeUnit.MILLISECONDS);
            // 正常请求，直接放行
            return chain.filter(exchange);
        }
        LOGGER.info("频率异常请求{}, ip:{}", url, ip);
        Result<String> result = Result.fail(CommonExceptionEnum.REPEAT_REQUEST);
        if ("/user/register".equals(url)) {
            // 计算剩余时间
            long lastTime = (long) lastRequest;
            long leftTime = RedisKeyConstant.REGISTER_REQUEST_DURATION - System.currentTimeMillis() + lastTime;
            String message = CommonExceptionEnum.REGISTER_FREQUENTLY.getMessage().replace("{expireTime}", TimeUtil.parseTime2HourMinSe(leftTime));
            result = Result.fail(CommonExceptionEnum.REGISTER_FREQUENTLY.getCode(), message);
        }
        // 短时间重复请求，报错
        return repeatRequestError(exchange, result);
    }

    public Mono<Void> repeatRequestError(ServerWebExchange exchange, Result<String> result) {
        ServerHttpResponse response = exchange.getResponse();
        // JSON格式返回
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(result));
            }catch (JsonProcessingException e) {
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
