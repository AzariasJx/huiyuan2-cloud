package com.huiyuan2.cloud.basic.network;

/**
 * @description: 网络包响应监听器
 * @author： 灰原二
 * @date: 2022/9/27 7:59
 */
public interface NettyPacketListener {

    /**
     * 收到消息
     * 注意：
     *      为了保证消息有序性，调用的eventloopGroup线程
     *      如果以后需要阻塞的场景，需要开启别的线程处理，否则会影响阻塞后续的网络包接收和处理
     * @throws Exception
     */
    void onMessage()throws Exception;
}
