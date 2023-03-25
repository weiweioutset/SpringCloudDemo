package com.cloud.demo.mq.bean;

import com.cloud.demo.constant.MQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author weiwei
 * @Date 2022/8/3 下午9:22
 * @Version 1.0
 * @Desc
 */
@Configuration
public class FriendTopicMQBean {

    @Bean
    public Queue topicQueueFriendAdd() {
        return new Queue(MQConstant.QUEUE_TOPIC_USER_SERVER_FRIEND_APPLY, true);
    }

    @Bean
    public Queue topicQueueFriendMoment() {
        return new Queue(MQConstant.QUEUE_TOPIC_USER_SERVER_FRIEND_MOMENT, true);
    }

    @Bean
    public TopicExchange topicExchangeUser() {
        return new TopicExchange(MQConstant.EXCHANGE_TOPIC_USER_SERVER_NORMAL, true, false);
    }

    @Bean
    public Binding topicBindingFriendAdd() {
        return BindingBuilder.bind(topicQueueFriendAdd()).to(topicExchangeUser()).with(MQConstant.ROUTING_TOPIC_USER_SERVER_APPLY);
    }

    @Bean
    public Binding topicBindingFriendMoment() {
        return BindingBuilder.bind(topicQueueFriendMoment()).to(topicExchangeUser()).with(MQConstant.ROUTING_TOPIC_USER_SERVER_MOMENT);
    }
}