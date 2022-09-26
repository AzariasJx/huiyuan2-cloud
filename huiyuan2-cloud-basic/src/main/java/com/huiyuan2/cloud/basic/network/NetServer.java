package com.huiyuan2.cloud.basic.network;

import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 网络服务端
 * @author： 灰原二
 * @date: 2022/9/26 7:41
 */
@Slf4j
public class NetServer {

    private final String name;
    private final EventLoopGroup boss;
    private final EventLoopGroup worker;
    private final boolean supportEpoll;

    public NetServer(String name, EventLoopGroup boss, EventLoopGroup worker, boolean supportEpoll) {
        this.name = name;
        this.boss = boss;
        this.worker = worker;
        this.supportEpoll = supportEpoll;
    }
}
