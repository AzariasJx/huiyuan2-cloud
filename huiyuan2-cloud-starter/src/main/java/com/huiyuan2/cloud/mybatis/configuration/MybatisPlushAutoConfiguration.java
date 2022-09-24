package com.huiyuan2.cloud.mybatis.configuration;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.huiyuan2.cloud.common.properties.YamlPropertyLoader;
import com.huiyuan2.cloud.mybatis.handler.Huiyuan2MetaObjectHandler;
import com.huiyuan2.cloud.mybatis.interceptor.SqlLogInterceptor;
import com.huiyuan2.cloud.mybatis.properties.MybatisProperties;
import lombok.AllArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: mybatis plus自动装配
 * @author： 灰原二
 * @date: 2022/9/24 13:15
 */
@AllArgsConstructor
@EnableConfigurationProperties(MybatisProperties.class)
@PropertySource(factory = YamlPropertyLoader.class,value = "classpath:huiyuan2-mybatis-plus.yml")
@MapperScan("com.huiyuan2.**.mapper.**")
public class MybatisPlushAutoConfiguration implements WebMvcConfigurer {

    /**
     * sql 日志拦截器
     * mybatis-plus.sql-log.enable 如果配置文件中没有配置，默认会开启SQL日志拦截器
     */
    @Bean
    @ConditionalOnProperty(value = "mybatis-plus.sql-log.enable", havingValue = "true", matchIfMissing = true)
    public SqlLogInterceptor sqlLogInterceptor() {
        return new SqlLogInterceptor();
    }


    /**
     * mp相关插件的拦截器
     * @return mp插件
     */
    @Bean
    @ConditionalOnProperty(value = "huiyuan2.mybatis.page-enable", havingValue = "true", matchIfMissing = false)
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件: PaginationInnerInterceptor
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 防止全表更新与删除插件: BlockAttackInnerInterceptor
        BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        interceptor.addInnerInterceptor(blockAttackInnerInterceptor);
        return interceptor;
    }

    /**
     * 公共字段自动填充数据 如：创建时间 修改时间
     */
    @Bean
    @ConditionalOnProperty(value = "huiyuan2.mybatis.auto-fill", havingValue = "true", matchIfMissing = false)
    @ConditionalOnMissingBean
    public MetaObjectHandler metaObjectHandler() {
        return new Huiyuan2MetaObjectHandler();
    }

}
