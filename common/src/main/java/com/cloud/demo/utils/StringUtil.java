package com.cloud.demo.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/5/7 下午10:41
 * @Version 1.0
 * @Desc 字符串处理工具类
 */
public class StringUtil extends StringUtils {
    /**
     * 将List转换为String
     * @param list
     * @param separator 分隔符
     * @return
     */
    public static String listToString(List<String> list, String separator) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (StringUtil.isEmpty(separator)) {
            separator = ",";
        }
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            if (first) {
                sb.append(str);
                first = false;
            } else {
                sb.append(separator).append(str);
            }
        }
        return sb.toString();
    }

    /**
     * 将String转换为List
     * @param source
     * @param separator 分隔符
     * @return
     */
    public static List<String> stringToList(String source, String separator) {
        if (isEmpty(source)) {
            return Collections.emptyList();
        }
        if (StringUtil.isEmpty(separator)) {
            separator = ",";
        }
        String[] str = source.split(separator);
        return Arrays.asList(str);
    }

    /**
     * 字符串转List
     * 字符串格式为： [ROLE_SYSTEM_ADMIN, ROLE_MEMBER]
     * @param source
     * @return
     */
    public static List<String> listStringToList(String source) {
        if (isEmpty(source)) {
            return Collections.emptyList();
        }
        return Arrays.asList(source.substring(1, source.length() - 1).replaceAll("\\s", ""));
    }
}
