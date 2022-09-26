package com.huiyuan2.cloud.basic.network;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 基础线程，添加一些日志参数
 * @author： 灰原二
 * @date: 2022/9/26 7:49
 */
@Slf4j
public class DefaultThread extends Thread{

    public DefaultThread(final String name,boolean daemon){
        super(name);
        configureThread(name,daemon);
    }
    public DefaultThread(final String name,boolean daemon,Runnable runnable){
        super(runnable,name);
        configureThread(name,daemon);
    }

    private void configureThread(String name,boolean daemon){
        setDaemon(daemon);
        setUncaughtExceptionHandler((t,e)->log.error("uncaught exception in thread '{}':",name,e));
    }

}
