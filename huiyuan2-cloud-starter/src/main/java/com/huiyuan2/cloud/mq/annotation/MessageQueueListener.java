package com.huiyuan2.cloud.mq.annotation;

import com.huiyuan2.cloud.mq.enums.MessageQueueType;

import java.lang.annotation.*;

/**
 * @description: 消息队列监听者注解，用于获取topic或者其他信息
 * @author： 灰原二
 * @date: 2022/9/24 11:35
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageQueueListener {

    /**
     * topic
     * @return
     */
    String topic();

    /**
     * 消费者组
     * @return
     */
    String consumerGroup() default "default-consumer-group";

    /**
     * 消息队列类型
     * @return
     */
    MessageQueueType type() default MessageQueueType.UNKONW;

    /**
     * tag
     * @return
     */
    String tags() default "*";
}

