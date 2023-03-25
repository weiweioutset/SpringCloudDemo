package com.cloud.demo.enums;

/**
 * @Author weiwei
 * @Date 2022/7/21 下午10:28
 * @Version 1.0
 * @Desc 好友添加方式枚举类
 */
public enum FromSourceEnums {
    QR_CODE("QR_CODE","二维码添加"),
    BUSINESS_CARD("BUSINESS_CARD","名片添加"),
    GROUP_ADD("GROUP_[${group_id}]_ADD","群组添加"),
    SEARCH_ADD("SEARCH_ADD","搜索添加"),
    ACCOUNT_ADD("ACCOUNT_ADD", "通过账号添加"),
    UNKNOWN_SOURCE("UNKNOWN_SOURCE", "未知来源"),
    ;

    FromSourceEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 来源代码
     */
    private String code;
    /**
     * 来源描述
     */
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
