package com.huiyuan2.cloud.mq.configuration;

import com.huiyuan2.cloud.mq.annotation.MessageQueueListener;
import com.huiyuan2.cloud.mq.consumer.KafkaConsumer;
import com.huiyuan2.cloud.mq.consumer.MessageListener;
import com.huiyuan2.cloud.mq.consumer.RocketMQConsumer;
import com.huiyuan2.cloud.mq.enums.MessageQueueType;
import com.huiyuan2.cloud.mq.producer.KafkaMessageQueue;
import com.huiyuan2.cloud.mq.producer.MessageQueue;
import com.huiyuan2.cloud.mq.producer.RocketMessageQueue;
import com.huiyuan2.cloud.mq.properties.MessageQueueProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @description: 消息队列自动装配
 * @author： 灰原二
 * @date: 2022/9/23 8:12
 */
@Configuration
@EnableConfigurationProperties(MessageQueueProperties.class)
public class MessageQueueAutoConfiguration {

    /**
     * kafaka默认配置 properties
     * 只有当配置开启enableProducer的时候才会引入
     * @param messageQueueProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name="kafkaProperties")
    @ConditionalOnProperty(value = "huiyuan2.mq.kafka.enableProducer",havingValue = "true")
    public Properties kafkaProperties(MessageQueueProperties messageQueueProperties){
        Properties properties = new Properties();
        properties.put("bootstrap.servers",messageQueueProperties.getKafka().getServer());
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");

        //acks=1 kafka 每次写入，只要能进入leader就行
        //acks=all 必须所有partition副本写入成功才行
        properties.put("acks","1");

        //producer 发出消息以后，必须等待response才会发送消息
        properties.put("max.in.flight.requests.per.connection","1");
        //写失败重试次数
        properties.put("retries",Integer.MAX_VALUE);
        //buffer大小和等待buffer填充时长
        properties.put("batch.size",16*1204);
        properties.put("linger.ms",10);
        properties.put("buffer.memory",32*1024*1024);

        return properties;
    }


    @Bean
    @ConditionalOnProperty(value = "huiyuan2.mq.kafka.enableProducer",havingValue = "true")
    public KafkaProducer<String,String> kafkaProducer(@Qualifier("kafkaProperties") Properties properties){
        return new KafkaProducer<>(properties);
    }

    @Bean(name = "kafkaMessageQueue")
    @ConditionalOnClass(KafkaProducer.class)
    @ConditionalOnProperty(value = "huiyuan2.mq.kafka.enableProducer", havingValue = "true")
    public MessageQueue kafkaMessageQueue(KafkaProducer<String, String> kafkaProducer) {
        return new KafkaMessageQueue(kafkaProducer);
    }

    @Bean
    @ConditionalOnProperty(value = "huiyuan2.mq.kafka.enableConsumer", havingValue = "true")
    public KafkaConsumer kafkaConsumer(MessageQueueProperties messageQueueProperties, List<MessageListener> listeners) {
        List<MessageListener> listenersResult =
                listeners.stream().filter(this::matchKafka).collect(Collectors.toList());
        return new KafkaConsumer(messageQueueProperties, listenersResult);
    }



    @Bean
    @ConditionalOnProperty(value = "huiyuan2.mq.rocketmq.enableProducer", havingValue = "true")
    public DefaultMQProducer defaultMQProducer(MessageQueueProperties messageQueueProperties) throws MQClientException {
        // rocketmq也是一样的，提取一些参数，基于Rocketmq的API，封装producer
        TransactionMQProducer transactionMQProducer =
                new TransactionMQProducer(messageQueueProperties.getRocketmq().getProducerGroup());
        transactionMQProducer.setNamesrvAddr(messageQueueProperties.getRocketmq().getServer());
        transactionMQProducer.start();
        return transactionMQProducer;
    }

    @Bean(name = "rocketMqMessageQueue")
    @ConditionalOnClass(DefaultMQProducer.class)
    @ConditionalOnProperty(value = "huiyuan2.mq.rocketmq.enableProducer", havingValue = "true")
    public MessageQueue rocketMessageQueue(DefaultMQProducer defaultMQProducer) {
        return new RocketMessageQueue(defaultMQProducer);
    }

    @Bean
    @ConditionalOnProperty(value = "huiyuan2.mq.rocketmq.enableConsumer",havingValue = "true")
    public RocketMQConsumer rocketMQConsumer(MessageQueueProperties messageQueueProperties,
                                             List<MessageListener> listeners) throws Exception{
        List<MessageListener> listenersFiltered = listeners.stream().filter(this::matchRocketMQ).collect(Collectors.toList());
        return new RocketMQConsumer(messageQueueProperties,listenersFiltered);
    }

    private boolean matchRocketMQ(MessageListener messageListener) {
        Class<? extends MessageListener> clazz = messageListener.getClass();
        if (!clazz.isAnnotationPresent(MessageQueueListener.class)) {
            return false;
        }
        MessageQueueListener annotation = clazz.getAnnotation(MessageQueueListener.class);
        return annotation.type().equals(MessageQueueType.ROCKETMQ);
    }

    private boolean matchKafka(MessageListener messageListener) {
        Class<? extends MessageListener> clazz = messageListener.getClass();
        if (!clazz.isAnnotationPresent(MessageQueueListener.class)) {
            return false;
        }
        MessageQueueListener annotation = clazz.getAnnotation(MessageQueueListener.class);
        return annotation.type().equals(MessageQueueType.KAFKA);
    }


}
