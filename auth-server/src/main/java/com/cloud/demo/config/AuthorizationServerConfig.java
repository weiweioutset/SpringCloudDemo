package com.cloud.demo.config;

import com.cloud.demo.constant.RedisKeyConstant;
import com.cloud.demo.handler.MyExtendAuthenticationEntryPointHandler;
import com.cloud.demo.handler.MyExtendOAuth2ResponseExceptionTranslator;
import com.cloud.demo.service.SingleTokenServices;
import com.cloud.demo.service.UserService;
import com.cloud.demo.utils.JwtTokenEnhancer;
import com.cloud.demo.utils.KeyPairUtils;
import com.cloud.demo.utils.MyPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/5/8 下午10:34
 * @Version 1.0
 * @Desc
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private MyPasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private MyExtendAuthenticationEntryPointHandler myExtendAuthenticationEntryPointHandler;
    @Autowired
    private JwtTokenEnhancer jwtTokenEnhancer;
    @Value("${client.secret}")
    private String clientSecret;



    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(accessTokenConverter());
        enhancerChain.setTokenEnhancers(delegates); //配置JWT的内容加强器
        endpoints.tokenServices(tokenServices());
        endpoints.exceptionTranslator(new MyExtendOAuth2ResponseExceptionTranslator()); // 设置自定义的异常解析器
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userService) //配置加载用户信息的服务
                .accessTokenConverter(accessTokenConverter())
                .reuseRefreshTokens(false)//refresh_tokens是否重复使用
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .tokenEnhancer(enhancerChain);
    }



    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //允许表单认证
        security.allowFormAuthenticationForClients();
        ClientCredentialsTokenEndpointFilter filter = new ClientCredentialsTokenEndpointFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationEntryPoint(myExtendAuthenticationEntryPointHandler);
        filter.afterPropertiesSet();
        security.addTokenEndpointAuthenticationFilter(filter);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //配置client_id
                .withClient("mobile_client")
                //配置client‐secret
                .secret(passwordEncoder.encode(clientSecret))
                //配置访问token的有效期
                .accessTokenValiditySeconds(RedisKeyConstant.TOKEN_EXPIRE_TIME_SECOND)
                //配置刷新token的有效期
                .refreshTokenValiditySeconds(RedisKeyConstant.REFRESH_TOKEN_EXPIRE_TIME_SECOND)
                //配置申请的权限范围
                .scopes("all")
                //配置grant_type，表示授权类型
                .authorizedGrantTypes("password", "refresh_token")
                .and()
                //配置client_id
                .withClient("pc_client")
                //配置client‐secret
                .secret(passwordEncoder.encode(clientSecret))
                //配置访问token的有效期
                .accessTokenValiditySeconds(RedisKeyConstant.TOKEN_EXPIRE_TIME_SECOND)
                //配置刷新token的有效期
                .refreshTokenValiditySeconds(RedisKeyConstant.REFRESH_TOKEN_EXPIRE_TIME_SECOND)
                //配置申请的权限范围
                .scopes("all")
                //配置grant_type，表示授权类型
                .authorizedGrantTypes("password", "refresh_token");
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(KeyPairUtils.generateKeyPair());
        return jwtAccessTokenConverter;
    }

    @Bean
    public SingleTokenServices tokenServices() {
        SingleTokenServices tokenServices = new SingleTokenServices();
        tokenServices.setTokenStore(redisTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setTokenEnhancer(jwtTokenEnhancer);
        return tokenServices;
    }

    @Bean
    public RedisTokenStore redisTokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix(RedisKeyConstant.REDIS_OAUTH_TOKEN_PREFIX);
        return redisTokenStore;
    }
}

