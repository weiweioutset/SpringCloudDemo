package com.cloud.demo.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/5/15 下午9:25
 * @Version 1.0
 * @Desc 网关白名单配置，放行指定的接口或者静态资源
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix="secure.ignore")
public class IgnoreResourceConfig {
    /**
     * 放行指定的接口
     */
    private List<String> urls;
    /**
     * 放行指定的静态资源/文件
     */
    private List<String> staticFile;
}

