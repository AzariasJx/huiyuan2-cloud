package com.huiyuan2.cloud.server.config;

/**
 * @description: 服务实例配置
 * @author： 灰原二
 * @date: 2022/9/28 23:22
 */
public interface ServerConfig {

    /**
     * 服务启动模式
     *
     * @return
     */
    LaunchMode launchMode();

    /**
     * 服务节点信息
     *
     * @return 例如：node1:234:1
     */
    String nodePeerServers();


    /**
     * 当前节点id
     */
    Integer nodeId();

    /**
     * 当前节点端口
     *
     * @return
     */
    Integer nodePort();

    /**
     * 节点存活时间阈值
     *
     * @return
     */
    Integer nodeAliveThresholdMs();

    /**
     * 普通节点向controller节点发送心跳的阈值
     *
     * @return
     */
    Integer controllerHeartbeatIntervalSec();

    /**
     * 多少次请求失败，就标记controller宕机
     *
     * @return
     */
    Integer markControillerDownThreshold();
}
