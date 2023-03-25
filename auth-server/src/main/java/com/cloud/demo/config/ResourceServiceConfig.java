package com.cloud.demo.config;

import com.cloud.demo.handler.MyExtendAuthenticationEntryPointHandler;
import com.cloud.demo.service.SingleTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @Author weiwei
 * @Date 2022/5/8 下午10:17
 * @Version 1.0
 * @Desc
 */
@Configuration
@EnableResourceServer
public class ResourceServiceConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private SingleTokenServices tokenServices;
    @Autowired
    private MyExtendAuthenticationEntryPointHandler myExtendAuthenticationEntryPointHandler;

    /**
     * token安全配置
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenServices);
        resources.authenticationEntryPoint(myExtendAuthenticationEntryPointHandler);
    }

    /**
     * 路由安全配置
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 对于登录接口，单独开放权限
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/token").permitAll()
                .antMatchers("/user/loginTest").permitAll()
                // 剩下的所有接口都要进行权限认证
                .anyRequest().authenticated()
                .and()
                .csrf().disable();

    }
}
