package com.cloud.demo.constant;

/**
 * @Author weiwei
 * @Date 2022/8/1 下午10:58
 * @Version 1.0
 * @Desc 消息队列常量池
 */// todo 拉取对方的动态到自己的时间轴(MQ异步)
                // todo 推送自己的动态到对方的(MQ异步)
public class MQConstant {
    /**
     * 常量名规范(全大写)
     * 交换机常量: EXCHANGE_交换机类型_服务名_交换机名, 例如:EXCHANGE_TOPIC_USER_SERVER_FRIEND
     * 队列常量: QUEUE_交换机类型_服务名_队列名, 例如:QUEUE_TOPIC_USER_SERVER_FRIEND
     * 路由规则常量:ROUTING_交换机类型_服务名_交换机名, 例如:ROUTING_TOPIC_USER_SERVER_FRIEND
     *
     * 常量值命名规范(全小写，点号隔开)
     * 交换机常量: 交换机类型 + 服务名 + Exchange, 例如:topicUserExchange
     * 队列常量: 交换机类型.服务名.队列名, 例如:topic.user.friend
     * 路由规则常量:交换机类型.服务名.#, 例如:topic.user.#
     */

    /**
     * 用户服务-好友消息服务-交换机名
     */
    public static final String EXCHANGE_TOPIC_USER_SERVER_NORMAL = "topicUserExchange";
    /**
     * 用户服务-好友消息服务-好友申请消息队列名
     */
    public static final String QUEUE_TOPIC_USER_SERVER_FRIEND_APPLY = "topic.user.friend.apply";
    /**
     * 用户服务-好友消息服务-好友动态相关队列名
     */
    public static final String QUEUE_TOPIC_USER_SERVER_FRIEND_MOMENT = "topic.user.friend.moment";
    /**
     * 用户服务-好友消息服务-路由建
     */
    public static final String ROUTING_TOPIC_USER_SERVER_ALL = "topic.routing.key.user.#";
    /**
     * 用户服务-好友申请消息路由建
     */
    public static final String ROUTING_TOPIC_USER_SERVER_APPLY = "topic.routing.key.user.apply";
    /**
     * 用户服务-好友动态相关路由建
     */
    public static final String ROUTING_TOPIC_USER_SERVER_MOMENT = "topic.routing.key.user.moment";
}
