package com.huiyuan2.cloud.lock.template;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 12:51
 */
public interface DistributedLock {

    /**
     * 阻塞加锁
     * @return
     * @throws Exception
     */
    boolean lock() throws Exception;

    /**
     * 加锁
     * @param time 超时时间
     * @param timeUnit 单位
     * @return
     * @throws Exception
     */
    boolean lock(int time, TimeUnit timeUnit) throws Exception;

    /**
     * 释放锁
     */
    void unlock();
}
