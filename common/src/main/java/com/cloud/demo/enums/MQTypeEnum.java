package com.cloud.demo.enums;

/**
 * @Author weiwei
 * @Date 2022/8/2 下午11:17
 * @Version 1.0
 * @Desc 消息队列类型枚举类
 */
public enum MQTypeEnum {
    /**
     * 常规类型
     */
    NORMAL,
    /**
     * 朋友添加申请
     */
    FRIEND_ADD_APPLY,
    /**
     * 朋友添加成功
     */
    FRIEND_ADD_SUCCESS,
    /**
     * 朋友添加失败
     */
    FRIEND_ADD_FAIL,
    /**
     * 拒绝添加好友
     */
    FRIEND_ADD_REJECT,
    /**
     * 删除好友
     */
    FRIEND_DELETE,
    /**
     * 拉取好友动态
     */
    FRIEND_MOMENTS_PULL,
    /**
     * 推送好友动态
     */
    FRIEND_MOMENTS_PUSH,
    /**
     * 清除对方动态时间轴中关于自己的所有动态
     */
    REMOVE_MY_MOMENTS,
    /**
     * 清除自己的动态时间轴中所有关于对方的动态
     */
    REMOVE_FRIEND_MOMENTS,
}
