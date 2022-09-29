package com.huiyuan2.cloud.server.instance;

import com.alibaba.fastjson.JSON;
import com.huiyuan2.cloud.basic.domain.CloudInstance;
import com.huiyuan2.cloud.basic.domain.MetadataChangedEventEnum;
import com.huiyuan2.cloud.basic.network.DefaultScheduler;
import com.huiyuan2.cloud.basic.network.proto.MetadataRequest;
import com.huiyuan2.cloud.basic.utils.Constants;
import com.huiyuan2.cloud.basic.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description: 服务注册中心实例
 * metadata 功能服务对外提供的数据
 * 可以设置监听器，数据有变化的时候就会回调监听器
 * @author： 灰原二
 * @date: 2022/9/29 22:43
 */
@Slf4j
public class RegistryDistributedServer extends AbstractDistributeServer {

    /**
     * 注册表
     * order-service ->{
     * order-service:localhost01:8080->instance1,
     * order-service:localhost02:8080->instance2
     * }
     */
    private final Map<String, Map<String, CloudInstance>> registryMap = new ConcurrentHashMap<>();

    /**
     * 监听数据变化的listener监听器
     */
    private final List<DistributedServerChangedListener> listeners = new ArrayList<>();

    /**
     * 服务注册中心功能的config 配置数据
     */
    private final RegistryConfig registryConfig;

    public RegistryDistributedServer(DefaultScheduler defaultScheduler, RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;

        defaultScheduler.schedule(
                "心跳超时检测",
                this::checkHeartbeatTimeout,
                0,
                registryConfig.getHeartbeatCheckIntervalSec(),
                TimeUnit.SECONDS
        );
    }

    @Override
    public String serviceType() {
        return Constants.REGISTRY_TYPE;
    }

    @Override
    public void addMetadata(String metadataKey, String metadataValue) {
        synchronized (this) {
            CloudInstance instance = JSON.parseObject(metadataValue, CloudInstance.class);
            instance.setRenewTime(System.currentTimeMillis());

            Map<String, CloudInstance> serviceMap = registryMap.computeIfAbsent(instance.getServiceName(), null);
            serviceMap.put(metadataKey, instance);

            invokeListener(metadataKey, metadataValue, MetadataChangedEventEnum.ADD);
        }
    }

    /**
     * 服务下线
     *
     * @param metadataKey
     */
    @Override
    public void removeMetadata(String metadataKey) {
        synchronized (this) {
            Map<String, CloudInstance> serviceMap = getServiceMap(metadataKey);
            if (serviceMap != null) {
                CloudInstance remove = serviceMap.remove(metadataKey);
                invokeListener(metadataKey, JSON.toJSONString(remove), MetadataChangedEventEnum.REMOVE);
            }
        }

    }

    private Map<String, CloudInstance> getServiceMap(String metadataKey) {
        //serviceName:ip:port
        return registryMap.get(metadataKey.split(":")[0]);
    }

    /**
     * 服务发现
     * @param metadataKeysPrefixList
     * @return
     */
    @Override
    public Map<String, String> fetchMeatdata(List<String> metadataKeysPrefixList) {
        HashMap<String, String> result = new HashMap<>();

        // 如果说传入的参数为空，此时会把你的注册表里的数据拿出来封装
        // 再返回
        if(metadataKeysPrefixList.isEmpty()){
            Collection<Map<String, CloudInstance>> values = registryMap.values();
            for (Map<String, CloudInstance> map : values) {
                for (String metadataKey : map.keySet()) {
                    CloudInstance instance = map.get(metadataKey);
                    if(null == instance){
                        return new HashMap<>();
                    }
                    result.put(metadataKey,JSON.toJSONString(instance));
                }
            }
        }

        for (String metadataKeyPrefix : metadataKeysPrefixList) {
            Map<String, CloudInstance> serviceMap = getServiceMap(metadataKeyPrefix);

            if(serviceMap == null){
                return  new HashMap<>();
            }

            List<String> metadataKeys = serviceMap.keySet().stream()
                    .filter(e -> e.startsWith(metadataKeyPrefix))
                    .collect(Collectors.toList());

            for (String metadataKey : metadataKeys) {
                CloudInstance instance = serviceMap.get(metadataKey);
                if (instance == null) {
                    return new HashMap<>();
                }
                result.put(metadataKey, JSON.toJSONString(instance));
            }
        }
        return result;
    }

    @Override
    public void clearMetadata() {
        registryMap.clear();
        for (DistributedServerChangedListener listener : listeners) {
            listener.onClearMetadata(serviceType());
        }
    }

    @Override
    public void heartbeat(MetadataRequest metadataRequest) {
        synchronized (this) {
            // request里有一个key，服务实例key，服务实例在上报心跳，key=服务实例key
            Map<String, CloudInstance> serviceMap = getServiceMap(metadataRequest.getKey());
            // 根据服务实例key从这个map里可以获取到一个服务实例对象
            CloudInstance ruyuanCloudInstance = serviceMap.get(metadataRequest.getKey());
            if (ruyuanCloudInstance != null) {
                if (log.isDebugEnabled()) {
                    log.debug("收到心跳请求：[key={}]", metadataRequest.getKey());
                }
                // 调用服务实例的续约的方法，实现一个续约就可以了
                ruyuanCloudInstance.renew();
            } else {
                // 做一个补偿，自动重新去进行注册动作就可以了
                addMetadata(metadataRequest.getKey(), metadataRequest.getValue());
                invokeListener(metadataRequest.getKey(), metadataRequest.getValue(), MetadataChangedEventEnum.ADD);
            }
        }
    }

    /**
     * 检查服务实例是否超时
     */
    private void checkHeartbeatTimeout() {
        synchronized (this) {
            for (Map.Entry<String, Map<String, CloudInstance>> entry : registryMap.entrySet()) {
                Map<String, CloudInstance> serviceMap = entry.getValue();
                Collection<CloudInstance> serviceInstances = serviceMap.values();
                List<CloudInstance> timeoutInstances = new ArrayList<>();
                for (CloudInstance instance : serviceInstances) {
                    if (instance.isTimeout(registryConfig.getHeartbeatTimeoutThresholdSec() * 1000L)) {
                        timeoutInstances.add(instance);
                    }
                }
                for (CloudInstance timeoutInstance : timeoutInstances) {
                    String metadataKey = timeoutInstance.getUniqueIdentity();
                    log.info("检测到服务续约过期，下线服务：【serviceUniqueIdentity={},renewTime={},currentTime={}",
                            metadataKey, DateUtils.format(new Date(timeoutInstance.getRenewTime())), DateUtils.format(new Date()));
                    serviceMap.remove(metadataKey);
                    invokeListener(metadataKey, JSON.toJSONString(timeoutInstance), MetadataChangedEventEnum.REMOVE);
                }
            }
        }
    }
}
