package com.huiyuan2.cloud.server.store.mysql;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huiyuan2.cloud.server.store.MetadataStorge;
import com.huiyuan2.cloud.server.store.mysql.domain.CloudMetadata;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/30 20:29
 */
public interface MysqlMetadataStorge extends IService<CloudMetadata>, MetadataStorge {
}
