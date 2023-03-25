package com.cloud.demo.mq.consumer;

import com.cloud.demo.entity.MQMessage;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * @Author weiwei
 * @Date 2022/8/7 下午12:49
 * @Version 1.0
 * @Desc
 */
//@Component
public class MyAckReceiver {

}
