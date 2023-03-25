package com.cloud.demo;

import com.cloud.demo.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableFeignClients
public class MomentsServerApplication {
    @Value("${file-upload.root-dir}")
    private String rootDir;

    @PostConstruct
    public void init() {
        System.out.println("程序初始化");
        System.out.println("##### 设置根目录 #####");
        FileUtils.init(rootDir);
    }

    public static void main(String[] args) {
        SpringApplication.run(MomentsServerApplication.class, args);
    }

}
