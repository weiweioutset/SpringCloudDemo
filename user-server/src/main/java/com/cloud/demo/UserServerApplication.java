package com.cloud.demo;

import com.cloud.demo.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.annotation.PostConstruct;

/**
 * @Author weiwei
 * @Date 2022/4/21 下午10:28
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class UserServerApplication {
    @Value("${file-upload.root-dir}")
    private String rootDir;

    @PostConstruct
    public void init() {
        System.out.println("程序初始化");
        System.out.println("##### 设置根目录 #####");
        FileUtils.init(rootDir);
    }

    public static void main(String[] args) {
        SpringApplication.run(UserServerApplication.class, args);
    }

}
