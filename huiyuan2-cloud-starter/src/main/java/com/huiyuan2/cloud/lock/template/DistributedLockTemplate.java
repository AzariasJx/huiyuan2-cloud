package com.huiyuan2.cloud.lock.template;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 12:50
 */
public interface DistributedLockTemplate {

    /**
     * 获得一个分布式锁对象
     * @param name
     * @return
     */
    DistributedLock getLock(String name);
}
