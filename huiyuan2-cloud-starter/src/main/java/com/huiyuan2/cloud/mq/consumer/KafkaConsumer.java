package com.huiyuan2.cloud.mq.consumer;

import com.huiyuan2.cloud.mq.annotation.MessageQueueListener;
import com.huiyuan2.cloud.mq.properties.MessageQueueProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 9:22
 */
@Slf4j
public class KafkaConsumer {
    private final List<MessageListener> messageListeners;
    private final MessageQueueProperties messageQueueProperties;

    public KafkaConsumer(MessageQueueProperties messageQueueProperties, List<MessageListener> messageListeners) {
        this.messageQueueProperties = messageQueueProperties;
        this.messageListeners = messageListeners;
        initConsumer();
    }
    private void initConsumer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", messageQueueProperties.getKafka().getServer());
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("enable.auto.commit", "false"); // 让kafka原生消费者，自动去提交offset，禁止了
        properties.put("auto.commit.interval.ms", "1000"); // 如果你要是开启自动提交offset，设置每隔多少时间提交一次
        properties.put("auto.offset.reset", "latest"); // 消费的时候，默认是从最近的一条消息开始进行消费
        if (messageListeners == null || messageListeners.isEmpty()) {
            return;
        }

        // 对我们自己手动实现和定义的每个listener，都会开一个线程，那个线程的话，他会启动一个kafka consumer
        // 不断的通过consumer拉取消息，拉取到的消息会传递给我们的listener回调
        // 再对这条消息去做一个ack
        for (MessageListener messageListener : messageListeners) {
            Processor processor = new Processor(messageListener, properties);
            processor.start();
        }
    }

    private class Processor extends Thread{
        private final MessageListener messageListener;

        private final org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer;

        public Processor(MessageListener messageListener,Properties properties){
            this.messageListener = messageListener;
            Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
            Properties copyProperties = new Properties();
            while (iterator.hasNext()){
                Map.Entry<Object, Object> next = iterator.next();
                copyProperties.put(next.getKey(),next.getValue());
            }
            copyProperties.put("group.id","business-group");
            setDaemon(true);
            String topic = topic(messageListener);
            setName("Kafka-Processor-Topic-"+topic);

            this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(copyProperties);
            //让原生的kafka consumer去订阅一个topic
            this.consumer.subscribe(Collections.singleton(topic));
        }


        @Override
        public void run() {
            while (isRun()){
                try{
                    //通过原生的kafka consumer poll拉取，设置timeout时间
                    //在1s内，如果没有拉取到任何信息，直接进行下一轮循环
                    ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofSeconds(1));
                    if(records==null || records.isEmpty()){
                        continue;
                    }

                    HashMap<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>(500);
                    for (ConsumerRecord<String, String> record : records) {
                        String value = record.value();
                        //拿出一条消息，同时保存消息的offset
                        offsets.put(new TopicPartition(record.topic(),record.partition()),
                                new OffsetAndMetadata(record.offset()+1)
                                );
                        //基于kafka原生consumer执行commit sync ,把保存的offset位置做一个提交
                        //如果某个消息过了很久没有commit ,会导致消息重复消费
                        messageListener.onMessage(value,()->this.consumer.commitSync(offsets));
                        offsets.clear();
                    }
                }catch (Exception e){
                    log.error("消费消息产生错误:",e);
                }
            }
        }
    }
    private boolean isRun() {
        return true;
    }


    private String topic(MessageListener messageListener) {
        Class<? extends MessageListener> messageListenerClazz = messageListener.getClass();
        MessageQueueListener annotation = messageListenerClazz.getAnnotation(MessageQueueListener.class);
        return annotation.topic();
    }
}
