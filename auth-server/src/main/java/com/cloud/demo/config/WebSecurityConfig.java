package com.cloud.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author weiwei
 * @Date 2022/5/8 下午10:35
 * @Version 1.0
 * @Desc
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // 身份验证管理器
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().permitAll()
                .and().authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/rsa/publicKey**").permitAll()
                .anyRequest().authenticated()
                .and().logout().permitAll()
                .and().csrf().disable();

    }
}
