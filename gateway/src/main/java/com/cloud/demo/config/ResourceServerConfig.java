package com.cloud.demo.config;

import com.cloud.demo.filter.IgnoreUrlsRemoveJwtFilter;
import com.cloud.demo.handler.AccessDeniedHandler;
import com.cloud.demo.handler.AuthenticationEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.PathContainer;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/5/15 下午9:05
 * @Version 1.0
 * @Desc 资源服务器配置
 */
@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
    private final AuthorizationManager authorizationManager;
    private final IgnoreResourceConfig ignoreResourceConfig;
    private final AccessDeniedHandler restfulAccessDeniedHandler;
    private final AuthenticationEntryPoint restAuthenticationEntryPoint;
    private final IgnoreUrlsRemoveJwtFilter ignoreUrlsRemoveJwtFilter;
    @Autowired
    private ReactiveRedisAuthenticationManager reactiveRedisAuthenticationManager;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        //认证过滤器
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveRedisAuthenticationManager);
        // Token解析器
        ServerBearerTokenAuthenticationConverter converter = new ServerBearerTokenAuthenticationConverter();
        // 设置允许解析链接中的token
        converter.setAllowUriQueryParameter(true);
        authenticationWebFilter.setServerAuthenticationConverter(converter);
        // 从Redis中读取Oauth2并鉴权
        http.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        //对白名单路径，直接移除JWT请求头
        http.addFilterBefore(ignoreUrlsRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        // 自定义匹配器，忽略静态资源的大小写问题
        List<ServerWebExchangeMatcher> matchers = new ArrayList<>();
        // 取出配置的所有需要放行的静态资源类型(需在配置中心配置)
        List<String> staticFiles = ignoreResourceConfig.getStaticFile();
        for (String staticFile : staticFiles) {
            String[] fileSuffixes = staticFile.split(",");
            for (String fileSuffix : fileSuffixes) {
                PathSuffixParserServerWebExchangeMatcher matcher = new PathSuffixParserServerWebExchangeMatcher(fileSuffix);
                matchers.add(matcher);
            }
        }

        http.authorizeExchange()
                .pathMatchers(ignoreResourceConfig.getUrls().toArray(new String[0])).permitAll()
                .matchers(new OrServerWebExchangeMatcher(matchers)).permitAll()
                .anyExchange().access(authorizationManager)
                .and().exceptionHandling()
                //处理未授权
                .accessDeniedHandler(restfulAccessDeniedHandler)
                //处理未认证
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and().csrf().disable();
        return http.build();
    }

}
