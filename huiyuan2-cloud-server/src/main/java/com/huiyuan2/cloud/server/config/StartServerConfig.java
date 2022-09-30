package com.huiyuan2.cloud.server.config;

import com.huiyuan2.cloud.basic.network.DefaultScheduler;
import com.huiyuan2.cloud.server.instance.ConfigCenterDistributedServer;
import com.huiyuan2.cloud.server.instance.DistributedServer;
import com.huiyuan2.cloud.server.instance.RegistryConfig;
import com.huiyuan2.cloud.server.instance.RegistryDistributedServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 开启服务的配置
 * @author： 灰原二
 * @date: 2022/9/29 22:05
 */
@Component
@Configuration
public class StartServerConfig implements InitializingBean {

    @Autowired
    private DefaultServerConfig defaultServerConfig;


    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultScheduler defaultScheduler = defaultScheduler();
        List<DistributedServer> distributedServers =
                Arrays.asList(registryServer(defaultScheduler),configServer());

    }

    @Bean
    public DefaultScheduler defaultScheduler(){
        return new DefaultScheduler("Node-Scheduler-");
    }

    public DistributedServer registryServer(DefaultScheduler defaultScheduler){
        RegistryConfig registryConfig = RegistryConfig.builder()
                .heartbeatCheckIntervalSec(30) //心跳检查间隔
                .heartbeatTimeoutThresholdSec(60) //心跳超时阈值
                .build();
        return new RegistryDistributedServer(defaultScheduler,registryConfig);
    }

    @Bean
    public ConfigCenterDistributedServer configServer(){
        return new ConfigCenterDistributedServer();
    }
}
