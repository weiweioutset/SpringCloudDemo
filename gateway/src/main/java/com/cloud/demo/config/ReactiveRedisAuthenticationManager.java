package com.cloud.demo.config;

import com.cloud.demo.constant.BaseConstant;
import com.cloud.demo.constant.RedisKeyConstant;
import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

/**
 * @Author weiwei
 * @Date 2022/6/26 下午1:19
 * @Version 1.0
 * @Desc
 */
@Component
public class ReactiveRedisAuthenticationManager implements ReactiveAuthenticationManager {
    private Logger logger = LoggerFactory.getLogger(ReactiveRedisAuthenticationManager.class);
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    private RedisTokenStore redisTokenStore;

    @PostConstruct
    public void init() {
        redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix(RedisKeyConstant.REDIS_OAUTH_TOKEN_PREFIX);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(a -> a instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class)
                .map(BearerTokenAuthenticationToken::getToken)
                .flatMap((accessToken -> {
                    logger.info("accessToken is :" + accessToken);
                    OAuth2AccessToken oAuth2AccessToken = redisTokenStore.readAccessToken(accessToken);
                    //根据access_token从数据库获取不到OAuth2AccessToken
                    if (oAuth2AccessToken == null || oAuth2AccessToken.isExpired()) {
                        return Mono.error(new CommonException(CommonExceptionEnum.NO_LOGIN_OR_TOKEN_INVALID));
                    }

                    OAuth2Authentication oAuth2Authentication = redisTokenStore.readAuthentication(accessToken);
                    if (oAuth2Authentication == null) {
                        return Mono.error(new CommonException(CommonExceptionEnum.INVALID_TOKEN));
                    } else {
                        return Mono.just(oAuth2Authentication);
                    }
                })).cast(Authentication.class);
    }
}
