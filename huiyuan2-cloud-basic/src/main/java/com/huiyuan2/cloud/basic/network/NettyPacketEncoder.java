package com.huiyuan2.cloud.basic.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/26 22:07
 */
public class NettyPacketEncoder extends MessageToByteEncoder<NettyPacket> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, NettyPacket nettyPacket, ByteBuf byteBuf) throws Exception {
        nettyPacket.write(byteBuf);
    }
}
