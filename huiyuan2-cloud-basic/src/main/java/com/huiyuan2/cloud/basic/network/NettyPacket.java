package com.huiyuan2.cloud.basic.network;

import com.google.common.graph.AbstractNetwork;
import com.google.protobuf.InvalidProtocolBufferException;
import com.huiyuan2.cloud.basic.network.proto.NettyPacketHeader;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 数据格式
 * @author： 灰原二
 * @date: 2022/9/26 22:08
 */
@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NettyPacket {

    /**
     * 消息体
     */
    protected byte[] body;

    /**
     * 请求头
     */
    private Map<String,String > header;


    public static NettyPacket copy(NettyPacket nettyPacket){
        return new NettyPacket(nettyPacket.getBody(),new ConcurrentHashMap<>(nettyPacket.getHeader()));
    }

    /***
     * 解包
     * @param byteBuf 内存缓冲区
     * @return netty网络包
     * @throws InvalidProtocolBufferException
     */
    public static NettyPacket parsePacket(ByteBuf byteBuf) throws InvalidProtocolBufferException {
        int headerLength = byteBuf.readInt();
        byte[] headerBytes = new byte[headerLength];
        byteBuf.readBytes(headerBytes);
        NettyPacketHeader nettyPacketHeader = NettyPacketHeader.parseFrom(headerBytes);

        int bodyLength = byteBuf.readInt();
        byte[] bodyBytes = new byte[bodyLength];
        byteBuf.readBytes(bodyBytes);
        return NettyPacket.builder()
                .header(new HashMap<>(nettyPacketHeader.getHeadersMap()))
                .body(bodyBytes)
                .build();
    }

    /**
     * 是否需要广播请求
     * @param broadcast 是否需要广播
     */
    public void setBroadcast(boolean broadcast){
        header.put("broadcast",String.valueOf(broadcast));
    }

    /**
     * 请求包类型
     * @return
     */
    public int getPacketType(){
        return Integer.parseInt(header.getOrDefault("packetType","0"));
    }
    /**
     * 获取广播请求的判断
     * @return
     */
    public boolean getBroadcast(){
        return Boolean.parseBoolean(header.getOrDefault("broadcast","false"));
    }
}
