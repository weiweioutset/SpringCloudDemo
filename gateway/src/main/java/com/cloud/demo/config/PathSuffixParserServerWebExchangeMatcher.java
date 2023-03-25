package com.cloud.demo.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author weiwei
 * @Date 2022/7/31 上午1:21
 * @Version 1.0
 * @Desc 自定义路由匹配器，用于匹配静态资源类型，根据后缀判断是否需要放行
 */
public final class PathSuffixParserServerWebExchangeMatcher implements ServerWebExchangeMatcher {
    private static final Log logger = LogFactory.getLog(PathSuffixParserServerWebExchangeMatcher.class);
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final String  suffix;

    public PathSuffixParserServerWebExchangeMatcher(String suffix) {
        Assert.notNull(suffix, "pattern cannot be null");
        this.suffix = suffix;
        // 忽略大小写问题
        antPathMatcher.setCaseSensitive(false);
    }

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        PathContainer path = request.getPath().pathWithinApplication();
        String pattern = "/**/*." + this.suffix;
        String pathStr = request.getPath().toString();
        boolean match = antPathMatcher.match(pattern, pathStr);
        if (!match) {
            return MatchResult.notMatch().doOnNext((result) -> {
                if (logger.isDebugEnabled()) {
                    logger.debug("Request '" + request.getMethod() + " " + path + "' doesn't match suffix : '"
                            + this.suffix + "'");
                }
            });
        }
        Map<String, String> pathVariables = antPathMatcher.extractUriTemplateVariables(pattern, pathStr);
        Map<String, Object> variables = new HashMap<>(pathVariables);
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "Checking match of request : '" + path + "'; against '" + this.suffix + "'");
        }
        return MatchResult.match(variables);
    }

    @Override
    public String toString() {
        return "PathSuffixParserServerWebExchangeMatcher{" + "suffix='" + this.suffix + '\''
                + '}';
    }

}
