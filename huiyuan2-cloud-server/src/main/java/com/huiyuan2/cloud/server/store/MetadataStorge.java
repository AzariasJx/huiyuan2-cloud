package com.huiyuan2.cloud.server.store;

import com.huiyuan2.cloud.server.store.mysql.domain.CloudMetadata;

import java.util.List;
import java.util.Set;

/**
 * @description: 元数据存储统一接口
 * @author： 灰原二
 * @date: 2022/9/30 20:15
 */
public interface MetadataStorge {

    /**
     * 根据槽位获取元数据列表
     * @param serviceType
     * @param slots
     * @return
     */
    List<CloudMetadata> loadBySlots(String serviceType, Set<Integer> slots);


    /**
     * 新增
     * @param serviceType
     * @param slot
     * @param metadataKey
     * @param metadataValue
     */
    void save(String serviceType,int slot,String metadataKey,String metadataValue);

    void remove(String serviceType,String metadataKey);
}
