package com.huiyuan2.cloud.es.template;

import com.huiyuan2.cloud.es.result.EsResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 14:22
 */
public interface EsTemplate {

    /**
     * 初始化 es索引的mapping元数据
     * @param indexName
     * @param exIndexMappingsJson
     * @return
     * @throws Exception
     */
    boolean initIndexMappings(String indexName,String exIndexMappingsJson) throws  Exception;


    /**
     * 保存或者更新文档
     *
     * @param indexName   索引名称
     * @param documentIndexObject 文档对象
     * @param documentId 文档id
     * @return
     * @throws Exception
     */
    boolean saveOrUpdate(String indexName,Object documentIndexObject , String documentId) throws Exception;

    /**
     * 根据索引名称 索引id 查询文档
     *
     * @param indexName 索引名称
     * @param id        文档id
     * @return 查询结果
     * @throws Exception 异常
     */
    String queryById(String indexName, String id) throws Exception;


    /**
     * 根据docId集合查询es文档
     *
     * @param indexName 索引名称
     * @param docsIds   订单id集合
     * @return 文档结果
     * @throws Exception 异常信息
     */
    MultiGetItemResponse[] multiGetOrderInfo(String indexName, List<String> docsIds) throws Exception;


    /**
     * 搜索结果
     *
     * @param indexName           索引名称
     * @param searchSourceBuilder 搜索条件构造器
     * @return 结果
     * @throws IOException 异常
     */
    EsResponse search(String indexName, SearchSourceBuilder searchSourceBuilder) throws IOException;


    /**
     * 更新文档
     *
     * @param indexName 索引名称
     * @param docId     文档id
     * @param o         文档值
     * @return 结果
     */
    boolean updateDoc(String indexName, String docId, Object o) throws IOException;

    /**
     * 更新文档
     *
     * @param indexName 索引名称
     * @param docId     文档id
     * @param map       文档map
     * @return 结果
     */
    boolean updateDoc(String indexName, String docId, Map<String, Object> map) throws IOException;

    /**
     * 删除文档
     *
     * @param indexName 索引名称
     * @param docId     文档id
     * @return 结果
     */
    boolean deleteDoc(String indexName, String docId) throws IOException;

    /**
     * 索引是否存在
     *
     * @param indexName 索引名称
     * @return 结果
     * @throws Exception 异常
     */
    boolean isIndexExists(String indexName) throws Exception;

    /**
     * 文档是否存在
     *
     * @param indexName 索引名称
     * @param docId     文档id
     * @return 结果
     * @throws IOException 异常
     */
    boolean isDocumentExists(String indexName, String docId) throws IOException;
}
