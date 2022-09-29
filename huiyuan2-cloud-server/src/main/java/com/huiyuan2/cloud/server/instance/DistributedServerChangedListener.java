package com.huiyuan2.cloud.server.instance;

import com.huiyuan2.cloud.basic.domain.MetadataChangedEventEnum;

/**
 * @description: 服务变更监听器
 * @author： 灰原二
 * @date: 2022/9/29 22:25
 */
public interface DistributedServerChangedListener {

    /**
     * 元数据更新通知
     * @param metadataKey
     * @param metadataValue
     * @param eventEnum 变更类型
     * @param serviceType 服务类型
     */
    void onMetadataChanged(String metadataKey, String metadataValue, MetadataChangedEventEnum eventEnum, String serviceType);

    /**
     * 清除元数据
     * @param serviceType
     */
    default void onClearMetadata(String serviceType){

    }
}
