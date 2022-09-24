package com.huiyuan2.cloud.mybatis.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.Properties;

/**
 * @description: 用于输出每条sql语句以及执行时间
 * @author： 灰原二
 * @date: 2022/9/24 13:21
 */
@Slf4j
public class SqlLogInterceptor implements Interceptor {
    private static final String DRUID_POOLED_PREPARED_STATEMENT= "com.alibaba.druid.pool.DruidPooledCallableStatement";

    private Method sqlMethod;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Statement statement;


        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
