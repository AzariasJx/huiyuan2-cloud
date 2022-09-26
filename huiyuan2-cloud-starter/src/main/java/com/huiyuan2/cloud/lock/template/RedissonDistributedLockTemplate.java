package com.huiyuan2.cloud.lock.template;

import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 12:54
 */
@AllArgsConstructor
public class RedissonDistributedLockTemplate implements DistributedLockTemplate{

    private RedissonClient redissonClient;


    @Override
    public DistributedLock getLock(String name) {
        RLock fairLock = redissonClient.getFairLock(name);

        //使用匿名内部类实现接口
        return new DistributedLock() {
            @Override
            public boolean lock() throws Exception {
                return fairLock.tryLock();
            }

            @Override
            public boolean lock(int time, TimeUnit timeUnit) throws Exception {
                return fairLock.tryLock(time,timeUnit);
            }

            @Override
            public void unlock() {
                if(fairLock.isHeldByCurrentThread()){
                    fairLock.unlock();
                }
            }
        };
    }
}
