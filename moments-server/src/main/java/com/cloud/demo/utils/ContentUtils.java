package com.cloud.demo.utils;

/**
 * @Author weiwei
 * @Date 2022/7/30 下午3:08
 * @Version 1.0
 * @Desc 内容处理工具
 */
public class ContentUtils {

    /**
     * 处理内容
     * @param content
     * @return
     */
    public static String processContent(String content) {
        if (StringUtil.isEmpty(content)) {
            return content;
        }
        // 替换特殊符号
        content = content.replaceAll("<script>", "&lt;script&gt;");
        // 替换敏感词
        return processSensitiveWords(content);
    }

    /**
     * 处理敏感词
     * @param content
     * @return
     */
    public static String processSensitiveWords(String content) {
        // todo 测试的
        if (StringUtil.isEmpty(content)) {
            return content;
        }
        return content.replaceAll("草你妈", "***").replaceAll("妈逼", "**");
    }
}
