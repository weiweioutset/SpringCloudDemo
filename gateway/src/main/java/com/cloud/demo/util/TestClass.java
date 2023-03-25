package com.cloud.demo.util;

import org.springframework.http.server.PathContainer;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @Author weiwei
 * @Date 2022/7/30 下午11:37
 * @Version 1.0
 * @Desc
 */
public class TestClass {
    public static void main(String[] args) {
        PathPatternParser pathPatternParser = new PathPatternParser();
        pathPatternParser.setCaseSensitive(false);  // 忽略大小写
//        pathPatternParser.setPathOptions(PathContainer.Options.create('/', false));
        String pathPattern = "{*jpg}";
        boolean result = pathPatternParser.parse(pathPattern).matches(PathContainer.parsePath("/image/moments/202207/7ca10fbd-d7a3-4d07-97c1-d1ebd7fb8518.jp4gdf"));
        System.out.println(result);
        AntPathMatcher antPathMatcher = new AntPathMatcher();
    }
}
