package com.cloud.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Author weiwei
 * @Date 2022/5/15 下午5:01
 * @Version 1.0
 * @Desc
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DataSourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataSourceApplication.class);
    }
}
