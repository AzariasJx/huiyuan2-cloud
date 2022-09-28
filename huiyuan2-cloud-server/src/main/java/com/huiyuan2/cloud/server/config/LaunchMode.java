package com.huiyuan2.cloud.server.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @description: 节点启动模式
 * @author： 灰原二
 * @date: 2022/9/28 23:23
 */
@Getter
@AllArgsConstructor
public enum LaunchMode {
    SINGLE(1, "single", "单机模式"),
    CLUSTER(2, "cluster", "集群模式"),
    ;
    private int value;
    private String mode;
    private String desc;

    public static LaunchMode getEnum(String mode) {
        for (LaunchMode value : values()) {
            if (value.mode.equals(mode)) {
                return value;
            }
        }
        return SINGLE;
    }
}
