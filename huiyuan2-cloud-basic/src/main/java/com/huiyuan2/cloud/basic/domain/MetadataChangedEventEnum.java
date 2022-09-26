package com.huiyuan2.cloud.basic.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * @description: 元数据修改事件枚举
 * @author： 灰原二
 * @date: 2022/9/26 7:32
 */
@Getter
@AllArgsConstructor
public enum MetadataChangedEventEnum {
    ADD(1),
    REMOVE(2),
    MODIFY(3),
    ;
    private final int value;


    public static MetadataChangedEventEnum getEnum(int value){
        for (MetadataChangedEventEnum metadataChangedEventEnum : values()) {
            if(metadataChangedEventEnum.getValue()==value){
                return metadataChangedEventEnum;
            }
        }
        return  ADD;
    }

}
