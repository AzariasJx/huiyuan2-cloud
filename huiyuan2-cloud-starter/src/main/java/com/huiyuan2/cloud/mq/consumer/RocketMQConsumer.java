package com.huiyuan2.cloud.mq.consumer;

import com.huiyuan2.cloud.mq.annotation.MessageQueueListener;
import com.huiyuan2.cloud.mq.properties.MessageQueueProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 9:22
 */
@Slf4j
public class RocketMQConsumer {
    private final List<MessageListener> messageListeners;
    private final MessageQueueProperties messageQueueProperties;

    public RocketMQConsumer(MessageQueueProperties messageQueueProperties,List<MessageListener> messageListeners) throws Exception{
        this.messageListeners = messageListeners;
        this.messageQueueProperties = messageQueueProperties;
        initConsumer();
    }

    private void initConsumer() throws Exception{

        for (MessageListener messageListener : messageListeners) {
            Class<? extends MessageListener> clazz = messageListener.getClass();
            MessageQueueListener annotation = clazz.getAnnotation(MessageQueueListener.class);
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(annotation.consumerGroup());
            consumer.setNamesrvAddr(messageQueueProperties.getRocketmq().getServer());

            /**
             * 在发送消息到topic的时候，每个消息都可以带上tags
             * 在进行消费的时候可以指定tag进行消费
             */
            consumer.subscribe(annotation.topic(),annotation.tags());
            consumer.registerMessageListener((MessageListenerConcurrently) (msgs,context)->{
                for (MessageExt msg : msgs) {
                    String message = new String(msg.getBody());
                    //使用自定义的listner进行消息回调
                    messageListener.onMessage(message);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
        }
    }
}
