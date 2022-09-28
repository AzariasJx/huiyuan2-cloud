package com.huiyuan2.cloud.basic.network;

import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @description: 默认的消息处理器
 * @author： 灰原二
 * @date: 2022/9/27 7:57
 */
@Slf4j
public class DefalutChannelHandler {

    private volatile SocketChannel socketChannel;

    private volatile boolean hasOtherHandlers = false;

//    private final List
}
