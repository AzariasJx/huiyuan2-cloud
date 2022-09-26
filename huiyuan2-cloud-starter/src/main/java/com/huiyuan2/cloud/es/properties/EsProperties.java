package com.huiyuan2.cloud.es.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description: es熟悉
 * @author： 灰原二
 * @date: 2022/9/24 14:18
 */
@Data
@ConfigurationProperties(prefix = "huiyuan2.es")
public class EsProperties {

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * es 连接地址集合
     */
    private List<String> clusterNodes = new ArrayList<>(Collections.singletonList("localhost:9200"));

    /**
     * 建立连接超时时间
     */
    private int connectionTimeoutMillis = 1000;

    /**
     * 数据传输超时时间
     */
    private int socketTimeoutMillis = 30000;

    /**
     * 从连接池获取连接的超时时间
     */
    private int connectionRequestTimeoutMillis = 500;

    /**
     * 路由节点的最大连接数
     */
    private int maxConnPerRoute = 10;


    /**
     * client的最大连接数
     */
    private int maxConnTotal = 30;

}
