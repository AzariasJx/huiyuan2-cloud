package com.huiyuan2.cloud.mybatis.interceptor;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.*;

/**
 * @description: 用于输出每条sql语句以及执行时间
 * @author： 灰原二
 * @date: 2022/9/24 13:21
 */
@Slf4j
@Intercepts({
        @Signature(type = StatementHandler.class,method = "query",args={Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class,method = "update",args = Statement.class),
        @Signature(type = StatementHandler.class,method = "batch",args = Statement.class )
})
public class SqlLogInterceptor implements Interceptor {
    private static final String DRUID_POOLED_PREPARED_STATEMENT= "com.alibaba.druid.pool.DruidPooledCallableStatement";

    private Method sqlMethod;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Statement statement;
        Object firstArg = invocation.getArgs()[0];

        if (Proxy.isProxyClass(firstArg.getClass())) {
            statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
        } else {
            statement = (Statement) firstArg;
        }

        MetaObject metaObject = SystemMetaObject.forObject(statement);

        try {
            statement = (Statement) metaObject.getValue("stmt.statement");
        } catch (Exception e) {
        }

        if (metaObject.hasGetter("delegate")) {
            try {
                statement = (Statement) metaObject.getValue("delegate");
            } catch (Exception ignored) {

            }
        }

        String originalSql = null;
        String stmtClassName = statement.getClass().getName();
        if (DRUID_POOLED_PREPARED_STATEMENT.equals(stmtClassName)) {
            try {
                if (sqlMethod == null) {
                    Class<?> clazz = Class.forName(DRUID_POOLED_PREPARED_STATEMENT);
                    sqlMethod = clazz.getMethod("getSql");
                }
                Object stmtSql = sqlMethod.invoke(statement);
                if (stmtSql instanceof String) {
                    originalSql = (String) stmtSql;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (originalSql == null) {
            originalSql = statement.toString();
        }
        originalSql = originalSql.replaceAll("[\\s]+", StringPool.SPACE);
        int index = indexOfSqlStart(originalSql);
        if (index > 0) {
            originalSql = originalSql.substring(index);
        }

        // 计算执行 SQL 耗时
        long start = SystemClock.now();
        Object result = invocation.proceed();
        long timing = SystemClock.now() - start;

        // SQL 打印执行结果
        Object target = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject1 = SystemMetaObject.forObject(target);
        MappedStatement ms = (MappedStatement) metaObject1.getValue("delegate.mappedStatement");

        // 打印 sql
        String sqlLogger = "\n\n==============  Sql Start  ==============" +
                "\nExecute ID  ：{}" +
                "\nExecute SQL ：{}" +
                "\nExecute Time：{} ms" +
                "\n==============  Sql  End   ==============\n";
        log.info(sqlLogger, ms.getId(), originalSql, timing);
        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * 获取sql语句开头部分
     *
     * @param sql ignore
     * @return ignore
     */
    private int indexOfSqlStart(String sql) {
        String upperCaseSql = sql.toUpperCase();
        Set<Integer> set = new HashSet<>();
        set.add(upperCaseSql.indexOf("SELECT "));
        set.add(upperCaseSql.indexOf("UPDATE "));
        set.add(upperCaseSql.indexOf("INSERT "));
        set.add(upperCaseSql.indexOf("DELETE "));
        set.remove(-1);
        if (CollectionUtils.isEmpty(set)) {
            return -1;
        }
        List<Integer> list = new ArrayList<>(set);
        list.sort(Comparator.naturalOrder());
        return list.get(0);
    }
}
