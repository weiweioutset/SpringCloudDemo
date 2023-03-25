package com.cloud.demo.config;

import cn.hutool.core.convert.Convert;
import com.cloud.demo.constant.BaseConstant;
import com.cloud.demo.constant.RedisKeyConstant;
import com.cloud.demo.utils.RedisUtils;
import com.cloud.demo.utils.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

/**
 * @Author weiwei
 * @Date 2022/5/15 下午9:44
 * @Version 1.0
 * @Desc 鉴权控制器，用于判断是否有资源的访问权限
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        //从Redis中获取当前路径可访问角色列表
        URI uri = authorizationContext.getExchange().getRequest().getURI();
        String urlPath = uri.getPath();

        Map<String,Object> authorityMap = redisUtils.hashEntries(RedisKeyConstant.RESOURCE_ROLES_MAP);
        List<String> authorities = new ArrayList<>();
        for (String key : authorityMap.keySet()) {
            if (UrlUtil.antPathMatch(key, urlPath)) {
                authorities.addAll(Convert.toList(String.class,authorityMap.get(key)));
            }
        }
        if (CollectionUtils.isEmpty(authorities)) {
            // 如果未匹配，则默认添加普通成员权限
            authorities.add(BaseConstant.AUTHORITY_DEFAULT_ROLE);
        }

        List<String> finalAuthorities = authorities;
        return mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(roleName -> {
                    // todo 上线时需要去掉
                    System.out.println("访问路径：" + urlPath);
                    System.out.println("用户角色roleName：" + roleName);
                    System.out.println("资源需要权限authorities：" + finalAuthorities);
                    return finalAuthorities.contains(roleName);
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(true));
    }
}
