package com.huiyuan2.cloud.es.configuration;

import com.huiyuan2.cloud.es.properties.EsProperties;
import com.huiyuan2.cloud.es.template.EsTemplate;
import com.huiyuan2.cloud.es.template.EsTemplateImpl;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.RestClients;

/**
 * @description:
 * @author： 灰原二
 * @date: 2022/9/24 14:13
 */
@Configuration
@ConditionalOnClass(RestClients.class)
@ConditionalOnProperty(value = "huiyuan2.es.enable",havingValue = "true")
@EnableConfigurationProperties(EsProperties.class)
public class ElasticsearchAutoConfiguration {

    /**
     * 初始化es操作模板
     * @param esProperties es属性
     * @return es操作模板
     */
    @Bean
    public EsTemplate esTemplate(EsProperties esProperties) {
        return new EsTemplateImpl(elasticsearchClient(esProperties));
    }

    /**
     * es客户端
     * @param esProperties 属性
     * @return es客户端
     */
    @Bean
    public RestHighLevelClient elasticsearchClient(EsProperties esProperties) {

        // 创建restClient的构造器
        RestClientBuilder restClientBuilder = RestClient.builder(loadHttpHosts(esProperties));
        // 设置连接超时时间等参数
        setConnectTimeOutConfig(restClientBuilder, esProperties);
        // 设置http相关的连接的参数
        setHttpClientConfig(restClientBuilder, esProperties);

        return new RestHighLevelClient(restClientBuilder);
    }

    /**
     * 加载es集群节点，逗号分隔
     *
     * @return 集群
     */
    private HttpHost[] loadHttpHosts(EsProperties esProperties) {
        int size = esProperties.getClusterNodes().size();
        HttpHost[] httpHosts = new HttpHost[size];

        for (int i = 0; i < size; i++) {
            String clusterNode = esProperties.getClusterNodes().get(i);
            String[] hostAndPort = clusterNode.split(":");
            httpHosts[i] = new HttpHost(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
        }
        return httpHosts;
    }

    /**
     * 配置连接超时时间等参数
     *
     * @param restClientBuilder 创建restClient的构造器
     * @param esProperties es参数配置
     */
    private void setConnectTimeOutConfig(RestClientBuilder restClientBuilder, EsProperties esProperties) {
        restClientBuilder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(esProperties.getConnectionTimeoutMillis());
            requestConfigBuilder.setSocketTimeout(esProperties.getSocketTimeoutMillis());
            requestConfigBuilder.setConnectionRequestTimeout(esProperties.getConnectionRequestTimeoutMillis());
            return requestConfigBuilder;
        });
    }

    /**
     * 使用异步httpclient时设置并发连接数
     *
     * @param restClientBuilder 创建restClient的构造器
     * @param esProperties es参数配置
     */
    private void setHttpClientConfig(RestClientBuilder restClientBuilder, EsProperties esProperties) {
        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(esProperties.getMaxConnTotal());
            httpClientBuilder.setMaxConnPerRoute(esProperties.getMaxConnPerRoute());
            return httpClientBuilder;
        });
    }


}
