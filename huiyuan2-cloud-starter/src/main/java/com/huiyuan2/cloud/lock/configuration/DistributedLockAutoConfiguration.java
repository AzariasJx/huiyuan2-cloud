package com.huiyuan2.cloud.lock.configuration;

import com.huiyuan2.cloud.common.constant.PropertiesPrefixConstant;
import com.huiyuan2.cloud.lock.properties.DistributedLockProperties;
import com.huiyuan2.cloud.lock.redisson.SingleRedissonConfig;
import com.huiyuan2.cloud.lock.template.RedissonDistributedLockTemplate;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 12:16
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DistributedLockProperties.class)
public class DistributedLockAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = PropertiesPrefixConstant.LOCK_DISTRIBUTED_LOCK_WORKMODE_PREFIX,havingValue = "standalone")
    public Config singleRedissonConfig(DistributedLockProperties distributedLockProperties){
        return SingleRedissonConfig.createRedissonConfig(distributedLockProperties);
    }

    @Bean
    @ConditionalOnClass(Redisson.class)
    @ConditionalOnProperty(value = PropertiesPrefixConstant.LOCK_DISTRIBUTED_LOCK_TYPE_PREFIX,havingValue = "redis")
    public RedissonDistributedLockTemplate redissonDistributedLockTemplate(Config config){
        //redis基础操作的客户端
        RedissonClient redissonClient = Redisson.create(config);

        return new RedissonDistributedLockTemplate(redissonClient);
    }

}
