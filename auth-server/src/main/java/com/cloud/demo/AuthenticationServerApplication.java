package com.cloud.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @Author weiwei
 * @Date 2022/4/21 下午10:28
 * @Version 1.0
 */
@SpringBootApplication
@EnableFeignClients
public class AuthenticationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServerApplication.class, args);
    }

}
