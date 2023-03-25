package com.cloud.demo.entity;

import com.cloud.demo.enums.MQTypeEnum;
import com.cloud.demo.utils.TimeUtil;

import java.io.Serializable;
import java.util.UUID;

/**
 * @Author weiwei
 * @Date 2022/8/2 下午9:16
 * @Version 1.0
 * @Desc MQ消息实体类
 */
public class MQMessage implements Serializable {

    public MQMessage() {
        this(MQTypeEnum.NORMAL);
    }

    public MQMessage(MQTypeEnum type) {
        this(type, null);
    }

    public MQMessage(MQTypeEnum type, Object msgBody) {
        // 设置消息类型
        this.type = type;
        // 设置id
        this.msgId = UUID.randomUUID().toString();
        // 设置消息创建时间
        this.msgTime = TimeUtil.getTimeStr(TimeUtil.PATTERN_E);
        // 设置消息内容(可以为空)
        this.msgBody = msgBody;
    }

    /**
     * 消息类型
     */
    private final MQTypeEnum type;
    /**
     * 消息id
     */
    private final String msgId;
    /**
     * 消息体
     */
    private Object msgBody;
    /**
     * 消息创建时间
     */
    private final String msgTime;

    public MQTypeEnum getType() {
        return type;
    }

    public String getMsgId() {
        return msgId;
    }

    public Object getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(Object msgBody) {
        this.msgBody = msgBody;
    }

    public String getMsgTime() {
        return msgTime;
    }

    @Override
    public String toString() {
        return "MQMessage{" +
                "type=" + type +
                ", msgId='" + msgId + '\'' +
                ", msgBody=" + msgBody +
                ", msgTime='" + msgTime + '\'' +
                '}';
    }
}
