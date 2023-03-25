package com.cloud.demo.mq.publisher;

import com.alibaba.fastjson.JSONObject;
import com.cloud.demo.constant.MQConstant;
import com.cloud.demo.entity.MQMessage;
import com.cloud.demo.enums.MQTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author weiwei
 * @Date 2022/8/2 下午8:56
 * @Version 1.0
 * @Desc
 */
@Component
public class BasicPublisher {
    private final Logger LOGGER = LoggerFactory.getLogger(BasicPublisher.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     */
    public void sendMessage(MQMessage message, String routingKey) {
        try {
            rabbitTemplate.setExchange(MQConstant.EXCHANGE_TOPIC_USER_SERVER_NORMAL);
            rabbitTemplate.setRoutingKey(routingKey);
            rabbitTemplate.convertAndSend(message);
            LOGGER.info("发送消息, {}", JSONObject.toJSONString(message));
        } catch (Exception e) {
            LOGGER.error("发送消息失败", e);
        }
    }

    /**
     * 发送好友申请
     * @param userId 当前账号id
     * @param friendId 朋友id
     * @param helloMsg 打招呼消息
     */
    public void sendFriendApplyMsg(Long userId, Long friendId, String helloMsg) {
        MQMessage message = new MQMessage(MQTypeEnum.FRIEND_ADD_APPLY);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("friendId", friendId);
        map.put("helloMsg", helloMsg);
        message.setMsgBody(map);
        sendMessage(message, MQConstant.ROUTING_TOPIC_USER_SERVER_APPLY);
    }

    /**
     * 添加好友成功，发送打招呼消息等
     */
    public void addFriendSuccessMsg(Long userId, Long friendId) {
        sendMessage(MQTypeEnum.FRIEND_ADD_SUCCESS, userId, friendId, MQConstant.ROUTING_TOPIC_USER_SERVER_APPLY);
    }

    /**
     * 拒绝好友申请
     */
    public void rejectFriendApplyMsg(Long userId, Long friendId, String rejectMsg) {
        MQMessage message = new MQMessage(MQTypeEnum.FRIEND_ADD_REJECT);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("friendId", friendId);
        map.put("helloMsg", rejectMsg);
        message.setMsgBody(map);
        sendMessage(message, MQConstant.ROUTING_TOPIC_USER_SERVER_APPLY);
    }

    /**
     * 拉取好友所有动态
     */
    public void pullFriendMomentMsg(Long userId, Long friendId) {
        sendMessage(MQTypeEnum.FRIEND_MOMENTS_PULL, userId, friendId, MQConstant.ROUTING_TOPIC_USER_SERVER_MOMENT);
    }

    /**
     * 推送自己的动态到好友时间轴
     */
    public void pushMyMomentMsg(Long userId, Long friendId) {
        sendMessage(MQTypeEnum.FRIEND_MOMENTS_PUSH, userId, friendId, MQConstant.ROUTING_TOPIC_USER_SERVER_MOMENT);
    }

    /**
     * 清除对方动态时间轴中关于自己的所有动态
     */
    public void removeMyMoments(Long userId, Long friendId) {
        sendMessage(MQTypeEnum.REMOVE_MY_MOMENTS, userId, friendId, MQConstant.ROUTING_TOPIC_USER_SERVER_MOMENT);
    }

    /**
     * 清除自己的动态时间轴中所有关于对方的动态
     */
    public void removeFriendMoments(Long userId, Long friendId) {
        sendMessage(MQTypeEnum.REMOVE_FRIEND_MOMENTS, userId, friendId, MQConstant.ROUTING_TOPIC_USER_SERVER_MOMENT);
    }

    /**
     * 删除好友，需要：
     * 清除聊天记录
     * 清除对方动态时间轴中关于自己的所有动态
     * 清除自己的动态时间轴中所有关于对方的动态
     * @param userId
     * @param friendId
     */
    public void deleteFriendMsg(Long userId, Long friendId) {
        sendMessage(MQTypeEnum.FRIEND_DELETE, userId, friendId, MQConstant.ROUTING_TOPIC_USER_SERVER_APPLY);
    }

    /**
     * 发送消息
     * @param type
     * @param userId
     * @param friendId
     */
    private void sendMessage(MQTypeEnum type, Long userId, Long friendId, String routingKey) {
        MQMessage message = new MQMessage(type);
        Map<String, Long> map = new HashMap<>();
        map.put("userId", userId);
        map.put("friendId", friendId);
        message.setMsgBody(map);
        sendMessage(message, routingKey);
    }
}
