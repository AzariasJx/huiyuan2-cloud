package com.huiyuan2.cloud.mybatis.domain;

import lombok.Data;

/**
 * @description: 公共分页
 * @author： 灰原二
 * @date: 2022/9/25 10:22
 */
@Data
public class CommonPage {
    private Integer current;

    private Integer size;
}
