package com.cloud.demo.enums;

/**
 * @Author weiwei
 * @Date 2022/7/12 下午9:52
 * @Version 1.0
 * @Desc 账号状态枚举类
 */
public enum AccountStatusEnum {
    USER_STATUS_APPLY(0,"申请中"),
    USER_STATUS_ENABLE(1,"有效"),
    USER_STATUS_DELETE(2,"账号已删除"),
    USER_STATUS_BLOCK(3,"账号被冻结"),
    ;

    AccountStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
