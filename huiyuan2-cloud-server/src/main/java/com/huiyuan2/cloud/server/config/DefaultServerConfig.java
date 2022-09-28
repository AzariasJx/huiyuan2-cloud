package com.huiyuan2.cloud.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/28 23:32
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "huiyuan2.cloud")
public class DefaultServerConfig implements ServerConfig {
    private Integer id;
    private Integer port;
    private String peerServers;
    private String launchMode;
    private Integer nodeAliveThresholdMs;
    private Integer controllerHeartbeatIntervalSec;
    private Integer markControllerDownThreshold;

    @Override
    public LaunchMode launchMode() {
        return LaunchMode.getEnum(launchMode);
    }

    @Override
    public String nodePeerServers() {
        return peerServers;
    }

    @Override
    public Integer nodeId() {
        return id;
    }

    @Override
    public Integer nodePort() {
        return port;
    }

    @Override
    public Integer nodeAliveThresholdMs() {
        return nodeAliveThresholdMs == null ? 180000 : nodeAliveThresholdMs;
    }

    @Override
    public Integer controllerHeartbeatIntervalSec() {
        return controllerHeartbeatIntervalSec == null ? 30 : controllerHeartbeatIntervalSec;
    }

    @Override
    public Integer markControillerDownThreshold() {
        return markControllerDownThreshold == null ? 3 : markControllerDownThreshold;
    }
}
