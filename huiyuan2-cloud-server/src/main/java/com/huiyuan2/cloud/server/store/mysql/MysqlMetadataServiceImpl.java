package com.huiyuan2.cloud.server.store.mysql;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huiyuan2.cloud.server.store.mysql.domain.CloudMetadata;

import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/30 20:31
 */
public class MysqlMetadataServiceImpl extends ServiceImpl<> implements MysqlMetadataStorge{
    @Override
    public List<CloudMetadata> loadBySlots(String serviceType, Set<Integer> slots) {
        return null;
    }

    @Override
    public void save(String serviceType, int slot, String metadataKey, String metadataValue) {

    }

    @Override
    public void remove(String serviceType, String metadataKey) {

    }
}
