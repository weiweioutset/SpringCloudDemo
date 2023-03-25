package com.cloud.demo.filter;

import com.alibaba.fastjson.JSONObject;
import com.cloud.demo.constant.BaseConstant;
import com.cloud.demo.constant.RedisKeyConstant;
import com.cloud.demo.vo.SecurityUser;
import com.nimbusds.jose.JWSObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

/**
 * @Author weiwei
 * @Date 2022/5/15 下午9:06
 * @Version 1.0
 * @Desc
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private static final String AUTHORIZE_PARAM = "access_token";
    private static final String AUTHORIZE_HEADER = "Authorization";
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    private RedisTokenStore redisTokenStore;

    @PostConstruct
    public void init() {
        redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix(RedisKeyConstant.REDIS_OAUTH_TOKEN_PREFIX);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        // 从请求头中获取token
        String token = headers.getFirst(AUTHORIZE_HEADER);
        if (StringUtils.isEmpty(token)) {
            // 如果请求头中没有，就从参数中获取
            token = exchange.getRequest().getQueryParams().getFirst(AUTHORIZE_PARAM);
        }
        if (StringUtils.isEmpty(token)) {
            // 白名单中的接口，不需要用户信息
            return chain.filter(exchange);
        }

        try {
            //从token中解析用户信息并设置到Header中去
            String accessToken = token.replace("Bearer ", "");
            OAuth2Authentication authentication = redisTokenStore.readAuthentication(accessToken);
            String userStr = authentication.getPrincipal().toString();
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            securityUser.setPassword(null);
            System.out.println("userStr2 = " + JSONObject.toJSONString(securityUser));
//            ServerHttpRequest request = exchange.getRequest().mutate().header("user", userStr).build();
//            exchange = exchange.mutate().request(request).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
