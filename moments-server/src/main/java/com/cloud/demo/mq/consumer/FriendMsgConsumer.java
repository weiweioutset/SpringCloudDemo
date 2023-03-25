package com.cloud.demo.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.cloud.demo.constant.MQConstant;
import com.cloud.demo.entity.MQMessage;
import com.cloud.demo.service.api.IMomentsService;
import com.cloud.demo.service.impl.MomentsService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Author weiwei
 * @Date 2022/8/3 下午9:23
 * @Version 1.0
 * @Desc 好友相关消息消费服务
 */
@Component
public class FriendMsgConsumer {
    private final Logger LOGGER = LoggerFactory.getLogger(FriendMsgConsumer.class);
    @Autowired
    private MomentsService momentsService;

    @RabbitListener(queues = MQConstant.QUEUE_TOPIC_USER_SERVER_FRIEND_APPLY, containerFactory = "singleListenerContainer")
    public void addFriendMessage(MQMessage message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        try {
            LOGGER.info("消费消息成功:{}", JSONObject.toJSONString(message));
            LOGGER.info("deliveryTag = " + tag);
            channel.basicAck(tag, false); //第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
        } catch (Exception e) {
            LOGGER.error("消费消息失败", e);
            channel.basicReject(tag, false);
        }
    }

    @RabbitListener(queues = MQConstant.QUEUE_TOPIC_USER_SERVER_FRIEND_MOMENT, containerFactory = "singleListenerContainer")
    public void friendMomentsMessage(MQMessage message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        try {
            LOGGER.info("消费消息成功:{}", JSONObject.toJSONString(message));
            LOGGER.info("deliveryTag = " + tag);
            Map<String, Integer> msgBody = (Map<String, Integer>) message.getMsgBody();
            Integer userId = msgBody.get("userId");
            Integer friendId = msgBody.get("friendId");
            if (userId == null || friendId == null) {
                LOGGER.info("动态消息消费失败，参数错误:TAG = {}, msgId = {}", tag, message.getMsgId());
                channel.basicReject(tag, false);
                return;
            }
            switch (message.getType()) {
                case FRIEND_MOMENTS_PULL:   // 拉取好友所有动态
                    momentsService.removeOrPushAllMomentsFromFriends(friendId, userId, true);
                    break;
                case FRIEND_MOMENTS_PUSH: //推送自己的动态给好友
                    momentsService.removeOrPushAllMomentsFromFriends(userId, friendId, true);
                    break;
                case REMOVE_MY_MOMENTS: // 清除对方动态时间轴中关于自己的所有动态
                    momentsService.removeOrPushAllMomentsFromFriends(userId, friendId, false);
                    break;
                case REMOVE_FRIEND_MOMENTS: // 清除自己的动态时间轴中所有关于对方的动态
                    momentsService.removeOrPushAllMomentsFromFriends(friendId, userId, false);
                    break;
                default:
                    LOGGER.info("消费消息失败，类型错误:{}", JSONObject.toJSONString(message));
                    break;
            }
            channel.basicAck(tag, false); //第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
        } catch (Exception e) {
            LOGGER.error("消费消息失败", e);
            LOGGER.info("消息消费失败:{}", JSONObject.toJSONString(message));
            channel.basicReject(tag, false);
        }
    }
}
