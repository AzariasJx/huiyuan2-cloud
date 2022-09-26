package com.huiyuan2.cloud.mybatis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @description: 日志打印
 * @author： 灰原二
 * @date: 2022/9/24 13:18
 */
@Data
@RefreshScope
@ConfigurationProperties("huiyuan2.mybatis")
public class MybatisProperties {

    /**
     * 是否打印sql
     */
    private boolean sql = true;

    /**
     * 是否开启分页插件
     */
    private boolean pageEnable = true;

    /**
     * 是否开启公共字段填充
     */
    private boolean autoFill = true;
}
