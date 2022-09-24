package com.huiyuan2.cloud.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 14:04
 */
public class Huiyuan2MetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Date date = new Date();
        this.strictInsertFill(metaObject,"createTime",Date.class,date);
        this.strictInsertFill(metaObject,"modifityTime",Date.class,date);
    }

    /**
     * 更新时，只更新修改时间
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "modifityTime", Date.class, new Date());
    }
}
