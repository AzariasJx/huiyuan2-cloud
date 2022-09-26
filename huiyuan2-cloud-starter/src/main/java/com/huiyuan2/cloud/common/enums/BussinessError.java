package com.huiyuan2.cloud.common.enums;

import lombok.Getter;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/25 10:31
 */
@Getter
public enum BussinessError {
    SYSTEM_UNKONW_ERROR(10001,"未知异常"),
    SYSTEM_OBJECT_NOT_FUND(10002,"请求资源不存在"),

    ;


    private Integer errCode;
    private String errMsg;

    BussinessError(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
