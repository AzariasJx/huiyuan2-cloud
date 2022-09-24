package com.huiyuan2.cloud.common.constant;

/**
 * @description:配置文件 配置项前缀 包名_前缀
 * @author： 灰原二
 * @date: 2022/9/24 12:41
 */
public interface PropertiesPrefixConstant {

    String COMMON_PREFIX = "huiyuan2";

    /**
     * 分布式锁启动类型
     */
    String LOCK_DISTRIBUTED_LOCK_WORKMODE_PREFIX = COMMON_PREFIX+ "." + "distributed.lock.redisWorkMode";

}
