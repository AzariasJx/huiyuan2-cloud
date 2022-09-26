package com.huiyuan2.cloud.lock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: redis部署方式
 * @author： 灰原二
 * @date: 2022/9/24 12:23
 */
@Getter
@AllArgsConstructor
public enum RedisWorkMode {
    WORK_MODE_STANDLONE("redis-single","单节点部署"),
    WORK_MODE_SENTINEL("redis-sentinel","哨兵部署"),
    WORK_MODE_CLUSTER("redis-cluster","集群部署")
    ;

    private String workMode;
    private String desc;
}
