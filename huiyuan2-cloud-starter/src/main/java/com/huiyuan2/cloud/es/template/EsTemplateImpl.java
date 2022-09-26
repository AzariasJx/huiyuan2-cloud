package com.huiyuan2.cloud.es.template;

import com.alibaba.fastjson.JSON;
import com.huiyuan2.cloud.es.result.EsResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @description: es操作模板
 * @author： 灰原二
 * @date: 2022/9/24 19:36
 */
@Slf4j
@AllArgsConstructor
public class EsTemplateImpl implements EsTemplate {

    /**
     * es客户端
     */
    private final RestHighLevelClient restHighLevelClient;


    /**
     * 初始化es索引的mappings元数据
     *
     * @param indexName
     * @param exIndexMappingsJson
     *
     * @return
     * @throws Exception
     */
    @Override
    public boolean initIndexMappings(String indexName, String exIndexMappingsJson) throws Exception {
        GetIndexRequest existIndexRequest = new GetIndexRequest(indexName);

        //查询指定索引是否存在
        boolean exists = restHighLevelClient.indices().exists(existIndexRequest, RequestOptions.DEFAULT);

        Request request;
        Response response;
        if (!exists) {
            //如果不存在就构造元数据请求
            request = new Request("PUT", indexName);
            request.setJsonEntity(exIndexMappingsJson);

            response = restHighLevelClient.getLowLevelClient().performRequest(request);
            log.info("初始化索引mappings的结构的请求结果为 [{}]", EntityUtils.toString(response.getEntity()));
            return true;
        }
        return false;
    }

    @Override
    public boolean saveOrUpdate(String indexName, Object documentIndexObject, String documentId) throws Exception {
        String userJson = JSON.toJSONString(documentIndexObject);
        IndexRequest indexRequest = new IndexRequest(indexName).source(userJson, XContentType.JSON);
        UpdateRequest updateRequest = new UpdateRequest(indexName, documentId).doc(userJson, XContentType.JSON).upsert(indexRequest);
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

        String index = updateResponse.getIndex();
        String docId = updateResponse.getId();
        long version = updateResponse.getVersion();
        log.info(String.format("Document update: index=%s, docId=%s, version=%s", index, docId, version));
        return updateResponse.getResult() == DocWriteResponse.Result.CREATED || updateResponse.getResult() == DocWriteResponse.Result.UPDATED || updateResponse.getResult() == DocWriteResponse.Result.NOOP;
    }

    @Override
    public String queryById(String indexName, String id) throws Exception {
        GetRequest getRequest = new GetRequest(indexName, id);
        GetResponse documentFields = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        return documentFields.getSourceAsString();
    }

    @Override
    public MultiGetItemResponse[] multiGetOrderInfo(String indexName, List<String> docsIds) throws Exception {
        return new MultiGetItemResponse[0];
    }

    @Override
    public EsResponse search(String indexName, SearchSourceBuilder searchSourceBuilder) throws IOException {
        return null;
    }

    @Override
    public boolean updateDoc(String indexName, String docId, Object o) throws IOException {
        return false;
    }

    @Override
    public boolean updateDoc(String indexName, String docId, Map<String, Object> map) throws IOException {
        return false;
    }

    @Override
    public boolean deleteDoc(String indexName, String docId) throws IOException {
        return false;
    }

    @Override
    public boolean isIndexExists(String indexName) throws Exception {
        return false;
    }

    @Override
    public boolean isDocumentExists(String indexName, String docId) throws IOException {
        return false;
    }
}
