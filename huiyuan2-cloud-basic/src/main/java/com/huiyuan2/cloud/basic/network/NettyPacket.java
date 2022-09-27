package com.huiyuan2.cloud.basic.network;

import com.google.common.graph.AbstractNetwork;
import com.google.protobuf.InvalidProtocolBufferException;
import com.huiyuan2.cloud.basic.network.constants.NettyPacketConstants;
import com.huiyuan2.cloud.basic.network.proto.NettyPacketHeader;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
     * 是否广播请求
     *
     * @param broadcast 是否需要广播
     */
    public void setBroadcast(boolean broadcast) {
        header.put(NettyPacketConstants.BROAD_CAST, String.valueOf(broadcast));
    }

    /**
     * 是否广播请求
     */
    public boolean getBroadcast() {
        return Boolean.parseBoolean(header.getOrDefault(NettyPacketConstants.BROAD_CAST, "false"));
    }


    /**
     * 设置请求序列号
     *
     * @param sequence 请求序列号
     */
    public void setSequence(String sequence) {
        if (sequence != null) {
            header.put(NettyPacketConstants.SEQUENCE, sequence);
        }
    }

    /**
     * 设置请求序列号
     */
    public String getSequence() {
        return header.get(NettyPacketConstants.SEQUENCE);
    }

    /**
     * 请求包类型
     *
     * @return 请求包类型
     */
    public int getPacketType() {
        return Integer.parseInt(header.getOrDefault(NettyPacketConstants.PACKET_TYPE, "0"));
    }

    /**
     * 设置请求包类型
     *
     * @param packetType 请求包类型
     */
    public void setPacketType(int packetType) {
        header.put(NettyPacketConstants.PACKET_TYPE, String.valueOf(packetType));
    }

    public void setError(String error) {
        header.put(NettyPacketConstants.ERROR, error);
    }

    public boolean isSuccess() {
        return getError() == null;
    }

    public boolean isError() {
        return !isSuccess();
    }

    public String getError() {
        return header.getOrDefault(NettyPacketConstants.ERROR, null);
    }

    public void setNodeId(int nodeId) {
        header.put(NettyPacketConstants.NODE_ID, String.valueOf(nodeId));
    }

    public int getNodeId() {
        String nodeId = header.getOrDefault(NettyPacketConstants.NODE_ID, "-1");
        return Integer.parseInt(nodeId);
    }

    public void setTimeoutInMs(long timeoutInMs) {
        header.put(NettyPacketConstants.TIMEOUT_IN_MS, String.valueOf(timeoutInMs));
    }

    public long getTimeoutInMs() {
        return Long.parseLong(header.getOrDefault(NettyPacketConstants.TIMEOUT_IN_MS, "0"));
    }

    public boolean isSupportChunked() {
        return Boolean.parseBoolean(header.getOrDefault(NettyPacketConstants.SUPPORT_CHUNKED, "false"));
    }

    public void setSupportChunked(boolean chunkedFinish) {
        header.put(NettyPacketConstants.SUPPORT_CHUNKED, String.valueOf(chunkedFinish));
    }

    public void setRedirectNodeId(int redirectNodeId) {
        header.put(NettyPacketConstants.REDIRECT_NODE_ID, String.valueOf(redirectNodeId));
    }

    public int getRedirectNodeId() {
        return Integer.parseInt(header.getOrDefault(NettyPacketConstants.REDIRECT_NODE_ID, "-1"));
    }

    public void setSlotKey(String key) {
        header.put(NettyPacketConstants.SLOT_KEY, key);
    }

    public String getSlotKey() {
        return header.get(NettyPacketConstants.SLOT_KEY);
    }

    public void setPeerServers(String peerServers) {
        header.put(NettyPacketConstants.PEER_SERVERS, peerServers);
    }

    public String getPeerServers() {
        return header.get(NettyPacketConstants.PEER_SERVERS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NettyPacket that = (NettyPacket) o;
        return that.getSequence().equals(getSequence());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSequence());
    }


    /**
     * 将数据写入bytebuf
     * @param byteBuf TODO
     */
    public void write(ByteBuf byteBuf) {
    }
}
