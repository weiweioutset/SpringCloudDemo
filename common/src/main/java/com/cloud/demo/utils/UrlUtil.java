package com.cloud.demo.utils;

import org.springframework.http.server.PathContainer;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @Author weiwei
 * @Date 2022/6/26 下午2:20
 * @Version 1.0
 * @Desc
 */
public class UrlUtil {
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 根据规则匹配地址
     * @param pattern
     * @param path
     * @return
     */
    public static boolean antPathMatch (String pattern, String path) {
        return antPathMatcher.match(pattern, path);
    }

    public static void main(String[] args) {
        System.out.println(antPathMatch("/**/*.png", "/image/moments/202207/test.png"));

    }

}
