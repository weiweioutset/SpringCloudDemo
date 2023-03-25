package com.cloud.demo.enums;

/**
 * @Author weiwei
 * @Date 2022/7/30 下午2:50
 * @Version 1.0
 * @Desc 动态发布范围枚举类
 */
public enum PublishScopeEnum {
    ALL("All", "公开"),
    PRIVATE("Private", "私密"),
    FRIEND("Friend", "朋友可见"),
    INCLUDE("Include", "部分可见"),
    EXCEPT("Except", "部分不可见")
    ;

    private String value;
    private String desc;

    PublishScopeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public boolean equal(String value) {
        return this.value.equals(value);
    }
}
