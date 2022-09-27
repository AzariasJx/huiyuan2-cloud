package com.huiyuan2.cloud.basic.network;

import com.huiyuan2.cloud.basic.utils.Constants;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import org.apache.commons.collections4.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @description: 基础的消息处理器
 * @author： 灰原二
 * @date: 2022/9/26 21:55
 */
public class BaseChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final List<AbstractChannelHandler> handlers = new LinkedList<>();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        socketChannel.pipeline().addLast(
                new NettyPacketDecoder(Constants.MAX_BYTES),//ChannelInboundHandlerAdapter
                //3个字节标识长度，最多支持16MB
                new LengthFieldPrepender(3),//ChannelOutboundHandlerAdapter
                new NettyPacketEncoder()//ChannelOutboundHandlerAdapter
        );

        for (AbstractChannelHandler handler : handlers) {
            socketChannel.pipeline().addLast(handler);
        }
    }

    /**
     * 添加自定义handler
     * @param handler
     */
    public void addHandler(AbstractChannelHandler handler){
        this.handlers.add(handler);
    }

    /**
     * 添加自定义handler
     * @param handlers
     */
    public void addHandlers(List<AbstractChannelHandler> handlers){
        if(CollectionUtils.isNotEmpty(handlers)){
            this.handlers.addAll(handlers);
        }
    }
}
