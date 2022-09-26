package com.huiyuan2.cloud.common.api;

import com.huiyuan2.cloud.common.enums.BussinessError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 响应对象封装
 * @author： 灰原二
 * @date: 2022/9/25 10:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> implements Serializable {
    private static final long serialVersionUID = -6043938676867547588L;
    private String status;

    private T data;

    public static <T> CommonResult<T> success(T data){
        return new CommonResult<>("success",data);
    }

    public static <BussinessError> CommonResult<BussinessError> fail(BussinessError error){
        return new CommonResult<>("fail",error);
    }

}
