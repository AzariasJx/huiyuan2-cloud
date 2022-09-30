package com.huiyuan2.cloud.server.store.mysql.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huiyuan2.cloud.mybatis.domain.CommonDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 元数据信息
 * @author： 灰原二
 * @date: 2022/9/30 20:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("cloud_metadata")
public class CloudMetadata extends CommonDomain {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 槽索引
     */
    private String slotIndex;

    /**
     * 服务类型
     */
    private String serverType;

    /**
     * 元数据key
     */
    private String metadataKey;

    /**
     * 元数据值
     */
    private String metadataValue;


}
