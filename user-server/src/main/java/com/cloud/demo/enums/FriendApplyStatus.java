package com.cloud.demo.enums;

/**
 * @Author weiwei
 * @Date 2022/7/24 下午12:53
 * @Version 1.0
 * @Desc
 */
public enum FriendApplyStatus {
    APPLY_ING(0, "申请中"),
    AGREE_APPLY(1, "同意申请"),
    REJECT_APPLY(2, "拒绝申请")
    ;

    FriendApplyStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    /**
     * 状态
     */
    private final Integer status;
    /**
     * 状态描述
     */
    private final String desc;

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
