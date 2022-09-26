package com.huiyuan2.cloud.basic.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;

/**
 * @description: 网络包编码
 * @author： 灰原二
 * @date: 2022/9/26 21:59
 */
public class NettyPacketDecoder extends LengthFieldBasedFrameDecoder {
    public NettyPacketDecoder(int maxFrameLength) {
        super(maxFrameLength,0,3,0,3);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        ByteBuf byteBuf = (ByteBuf)super.decode(ctx, buffer);
        if(byteBuf!=null){
            try {
                return NettyPacket.parsePacket(byteBuf);
            } finally {
                ReferenceCountUtil.release(byteBuf);
            }
        }
        return null;
    }
}
