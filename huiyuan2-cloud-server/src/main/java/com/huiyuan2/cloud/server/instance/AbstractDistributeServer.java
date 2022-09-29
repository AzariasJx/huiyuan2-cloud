package com.huiyuan2.cloud.server.instance;

import com.huiyuan2.cloud.basic.domain.MetadataChangedEventEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/29 22:38
 */
public abstract class AbstractDistributeServer implements DistributedServer{

    private final List<DistributedServerChangedListener> listeners = new ArrayList<>();

    @Override
    public void addMetadataChangedListener(DistributedServerChangedListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeMetadataChangedListener(DistributedServerChangedListener listener) {
        this.listeners.remove(listener);
    }



    protected void invokeListener(String metadataKey, String metadataValue, MetadataChangedEventEnum changedEventEnum){
        for (DistributedServerChangedListener listener : listeners) {
            listener.onMetadataChanged(metadataKey,metadataValue,changedEventEnum,serviceType());
        }
    }

    @Override
    public void modifyMetadata(String metadataKey, String metadata) {

    }
}
