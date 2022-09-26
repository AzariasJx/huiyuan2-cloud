package com.huiyuan2.cloud.lock.redisson;

import com.huiyuan2.cloud.lock.constant.RedissonConstant;
import com.huiyuan2.cloud.lock.properties.DistributedLockProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;
/**
 * @description: 单机模式部署redisson
 * @author： 灰原二
 * @date: 2022/9/24 12:31
 */
@Slf4j
public class SingleRedissonConfig {
    public static Config createRedissonConfig(DistributedLockProperties redissonProperties){
        Config config = new Config();

        try{
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int redisDatabase = redissonProperties.getRedisDatabase();
            String redisAddr = RedissonConstant.REDISSION_CONNECTION_PREFIX+ address;
            config.useSingleServer().setAddress(redisAddr);
            config.useSingleServer().setDatabase(redisDatabase);
            if(StringUtils.isNotBlank(password)){
                config.useSingleServer().setPassword(password);
            }
            log.info("初始化 standalone redisAddress={}",redisAddr);
            
        }catch (Exception e){
            log.error("standalone redisson init error",e);
            e.printStackTrace();
        }
        return  config;
    }

}
