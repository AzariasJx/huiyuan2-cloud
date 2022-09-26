package com.huiyuan2.cloud.es.result;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 19:35
 */
@Data
public class EsResponse {
    private long total;

    private JSONArray data;
}
