package com.huiyuan2.cloud.lock.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 12:18
 */
@Data
@ConfigurationProperties(prefix = "huiyuan2.distruted.lock")
public class DistributedLockProperties {

    /**
     * 分布式锁类型
     */
    private String type;

    /**
     * ip:port  用逗号分隔   127.0.0.1:3306,127.0.0.1:3307
     */
    private String address;

    /**
     * 连接密码
     */
    private String password;

    /**
     * redis server的工作方式
     */
    private String redisWorkMode;

    /**
     * 选取数据库
     */
    private int redisDatabase;
}
