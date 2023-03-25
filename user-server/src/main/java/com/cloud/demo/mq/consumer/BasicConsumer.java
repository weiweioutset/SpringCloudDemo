package com.cloud.demo.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.cloud.demo.constant.MQConstant;
import com.cloud.demo.entity.MQMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author weiwei
 * @Date 2022/8/2 下午8:59
 * @Version 1.0
 * @Desc
 */
@Component
public class BasicConsumer {
    private final Logger LOGGER = LoggerFactory.getLogger(BasicConsumer.class);

//    @RabbitListener(queues = MQConstant.QUEUE_TOPIC_USER_SERVER_FRIEND_APPLY, containerFactory = "singleListenerContainer")
//    public void addFriendMessage(MQMessage message) {
//        try {
//            LOGGER.info("消费消息成功:{}", JSONObject.toJSONString(message));
//        } catch (Exception e) {
//            LOGGER.error("消费消息失败", e);
//        }
//    }
//
//    @RabbitListener(queues = MQConstant.QUEUE_TOPIC_USER_SERVER_FRIEND_MOMENT, containerFactory = "singleListenerContainer")
//    public void friendMomentsMessage(MQMessage message) {
//        try {
//            LOGGER.info("消费消息成功2:{}", JSONObject.toJSONString(message));
//        } catch (Exception e) {
//            LOGGER.error("消费消息失败", e);
//        }
//    }
//    @RabbitListener(queues = MQConstant.QUEUE_TOPIC_USER_SERVER_FRIEND_MOMENT)
//    public void receiveMessage(MQMessage message) {
//        LOGGER.info("消费消息成功23434:{}", JSONObject.toJSONString(message));
//    }
}
