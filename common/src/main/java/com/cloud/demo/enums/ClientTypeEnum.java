package com.cloud.demo.enums;


/**
 * @Author weiwei
 * @Date 2022/7/12 下午9:29
 * @Version 1.0
 * @Desc 客户端类型枚举类
 */
public enum ClientTypeEnum {
    CLIENT_PC("pc_client","PC"),
    CLIENT_MOBILE("mobile_client","Mobile"),
    CLIENT_H5("h5_client","H5"),
    CLIENT_ALL("All","All"),
    CLIENT_UNKNOWN("unknown_client","Unknown"),
    ;

    ClientTypeEnum(String value, String type) {
        this.value = value;
        this.type = type;
    }

    private final String value;
    private final String type;

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    /**
     * 根据id获取枚举类
     * @param clientId
     * @return
     */
    public static ClientTypeEnum getEnum(String clientId) {
        switch (clientId) {
            case "pc_client":
                return CLIENT_PC;
            case "mobile_client":
                return CLIENT_MOBILE;
            case "h5_client":
                return CLIENT_H5;
            case "All":
                return CLIENT_ALL;
            default:
                return CLIENT_UNKNOWN;
        }
    }
}
