package com.huiyuan2.cloud.server.instance;

import com.huiyuan2.cloud.basic.network.proto.MetadataRequest;

import java.util.List;
import java.util.Map;

/**
 * @description: 具体的服务功能
 * @author： 灰原二
 * @date: 2022/9/29 22:14
 */
public interface DistributedServer {
    /**
     * 名称
     * @return
     */
    String serviceType();

    /**
     * 保存元数据
     * @param metadataKey
     * @param metadataValue
     */
    void addMetadata(String metadataKey,String metadataValue);

    /**
     * 删除元数据
     * @param metadataKey
     */
    void removeMetadata(String metadataKey);

    /**
     * 获取元数据
     * @param metadataKeys
     * @return
     */
    Map<String,String> fetchMeatdata(List<String> metadataKeys);


    /**
     * 清楚所有元数据
     */
    void clearMetadata();

    /**
     * 修改元数据
     * @param metadataKey
     * @param metadata
     */
    void modifyMetadata(String metadataKey,String metadata);

    /**
     * 心跳
     */
    default void heartbeat(MetadataRequest metadataRequest){}


    /**
     * 添加数据变更监听器
     * @param listener
     */
    void addMetadataChangedListener(DistributedServerChangedListener listener);

    /**
     * 移除监听器
     * @param listener
     */
    void removeMetadataChangedListener(DistributedServerChangedListener listener);
}
