package com.huiyuan2.cloud.basic.network;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.MDC;

import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 默认调度器
 * @author： 灰原二
 * @date: 2022/9/26 7:43
 */
@Slf4j
public class DefaultScheduler {

    private ScheduledThreadPoolExecutor executor;
    private final AtomicInteger schedulerThreadId = new AtomicInteger(0);
    private final AtomicBoolean shutdown = new AtomicBoolean(true);

    public DefaultScheduler(String threadNameprefix,int threads,boolean daemon){
        if(shutdown.compareAndSet(true,false)){
            executor = new ScheduledThreadPoolExecutor(threads,
                    runnable-> new DefaultThread(threadNameprefix+schedulerThreadId.getAndIncrement(),daemon,runnable));
            //当对线程池进行关闭以后，已经存在的调度任务是否需要继续
            executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
            //关闭以后，延迟任务是否需要继续
            executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        }
    }


    /**
     * 调度任务
     * 不需要延迟调度的时候，可以直接执行
     * @param name
     * @param r
     */
    public void scheduleOnce(String name,Runnable r){
        scheduleOnce(name,r,0);
    }

    public void scheduleOnce(String name,Runnable r,long delay){
        schedule(name,r,delay,0,TimeUnit.MILLISECONDS);
    }
    /**
     * 调度任务
     *
     * @param name 任务名称
     * @param r 任务
     * @param delay 延迟执行时间
     * @param period 调度周期
     * @param timeUnit 时间单位
     * @return
     */
    public ScheduledFuture<?> schedule(String name, Runnable r, long delay, long period, TimeUnit timeUnit){
        if(log.isDebugEnabled()){
            log.debug("Scheduling task {} with initial delay{} {} and period {} {} .",name,delay,timeUnit,period,timeUnit);
        }

        Runnable delegate = ()->{
            try {
                if(log.isTraceEnabled()){
                    log.trace("beginning execution of scheduled task {}",name);
                }

                String loggerId = DigestUtils.md5Hex("" + System.nanoTime() + new Random().nextInt());
                MDC.put("logger_id",loggerId);

                r.run();
            } catch (Throwable e) {
                log.error("Uncaught exception in scheduled task{}:",name,e);
            }finally {
                if(log.isTraceEnabled()){
                    log.trace("Completed execution of scheduled task {}.", name);
                }
                MDC.remove("logger_id");
            }


        };
        if(shutdown.get()){
            return null;
        }

        /**
         * 如果period>0 说明需要周期执行
         */
        if(period>0){
            return executor.scheduleWithFixedDelay(delegate,delay,period,timeUnit);
        }else {
            return executor.schedule(delegate,delay,timeUnit);
        }

    }

    /**
     * 优雅停止
     */
    public void shutdown(){
        if(shutdown.compareAndSet(false,true)){
            log.info("shutdown defaultScheduler.");
            executor.shutdown();
        }
    }
}
