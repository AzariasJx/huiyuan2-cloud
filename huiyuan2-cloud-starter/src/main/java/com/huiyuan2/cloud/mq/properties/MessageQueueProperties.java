package com.huiyuan2.cloud.mq.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.lang.ref.PhantomReference;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 8:41
 */
@Data
@ConfigurationProperties(prefix = "huiyuan2.mq")
public class MessageQueueProperties {
    private Kafka kafka;
    private Rocketmq rocketmq;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Kafka{
        private Boolean enableProducer;
        private Boolean enableConsumer;
        private String sever;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Rocketmq{
        private Boolean enableProducer;
        private Boolean enableConsumer;
        private String server;
        private String producerGroup = "huiyuan2-cloud-group";
    }

}
