package com.cloud.demo.enums;

/**
 * @Author weiwei
 * @Date 2022/7/29 下午10:32
 * @Version 1.0
 * @Desc 文件名生成策略
 */
public enum FileNameGenerateStrategy {
    // 源文件名,默认方式
    SOURCE_FILE_NAME,
    // 时间戳格式
    TIMESTAMP_NAME,
    // 日期格式
    DATE_NAME,
    // 新文件名
    NEW_NAME,
    // UUID的形式
    UUID_NAME
}
