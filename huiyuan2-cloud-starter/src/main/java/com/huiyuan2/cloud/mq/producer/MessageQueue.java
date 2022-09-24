package com.huiyuan2.cloud.mq.producer;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 9:06
 */
public interface MessageQueue {

    /**
     * 发送消息
     * @param topic
     * @param message
     */
    void send(String topic,String message) throws MessageQueueException;

    /**
     * 发送消息
     *
     * @param topic          topic
     * @param message        message
     * @param tags           RocketMq生效
     */
    void send(String topic, String message, String tags) throws MessageQueueException;

    /**
     * 发送消息
     *
     * @param topic          topic
     * @param message        message
     * @param tags           RocketMq生效
     * @param key            RocketMQ唯一的Key,Kafka用于分区的key
     */
    void send(String topic, String message, String tags, String key) throws MessageQueueException;

    /**
     * 发送消息
     *
     * @param topic          topic
     * @param message        message
     * @param tags           RocketMq生效
     * @param key            RocketMQ唯一的Key,Kafka用于分区的key
     * @param delayTimeLevel 延迟时间，kafka不支持
     */
    void send(String topic, String message, String tags, String key, Integer delayTimeLevel) throws MessageQueueException;

}
