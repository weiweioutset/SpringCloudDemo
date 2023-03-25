package com.cloud.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author weiwei
 * @Date 2022/8/4 下午8:46
 * @Version 1.0
 * @Desc
 */
@Configuration
public class RabbitMQConfig {
    private final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);
    @Autowired
    private CachingConnectionFactory connectionFactory;
//    @Autowired
//    private MyAckReceiver myAckReceiver;

    /**
     * 下面为单一消费者实例的配置
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer() {
        //定义消息监听器所在的容器工厂
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //设置容器工厂所用的实例
        factory.setConnectionFactory(connectionFactory);
        //设置消息在传输中的格式，在这里采用JSON的格式进行传输
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
//        //设置并发消费者实例的初始数量。在这里为1个
//        factory.setConcurrentConsumers(1);
//        //设置并发消费者实例的最大数量。在这里为5个
//        factory.setMaxConcurrentConsumers(5);
//        //设置并发消费者实例中每个实例拉取的消息数量-在这里为1个
//        factory.setPrefetchCount(1);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    //自定义配置RabbitMQ发送消息的操作组件RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(){
        //设置“发送消息后进行确认”
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        //设置“发送消息后返回确认信息”
        connectionFactory.setPublisherReturns(true);
        //构造发送消息组件实例对象
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        //发送消息后，如果发送成功，则输出“消息发送成功”的反馈信息
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                LOGGER.info("消息队列消费失败,cause:[{}]", cause);
            }
        });
        //发送消息后，如果发送失败，则输出“消息发送失败-消息丢失”的反馈信息
        rabbitTemplate.setReturnsCallback(returned -> LOGGER.info("消息发送失败[{}]", returned));
        //定义消息传输的格式为JSON字符串格式
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        //最终返回RabbitMQ的操作组件实例RabbitTemplate
        return rabbitTemplate;
    }
}
