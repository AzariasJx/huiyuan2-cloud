package com.huiyuan2.cloud.server.instance;

import lombok.Builder;
import lombok.Data;

/**
 * @description: 服务注册中心配置信息
 * @author： 灰原二
 * @date: 2022/9/29 22:35
 */
@Data
@Builder
public class RegistryConfig {

    /**
     * 心跳检查间隔
     */
    private int heartbeatCheckIntervalSec;

    /**
     * 心跳超时检测阈值
     */
    private int heartbeatTimeoutThresholdSec;
}
