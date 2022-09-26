package com.huiyuan2.cloud.basic.network;

import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/26 21:52
 */
@Slf4j
public class NetClient {

    //和其他节点建立的连接
    private  String name;

    private  DefaultScheduler defaultScheduler;

    private  EventLoopGroup conectThreadGroup;

}
