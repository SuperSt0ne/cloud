package com.stone.search.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

@Configuration
public class EsConfig {

//    @Override
    @Bean
    public RestHighLevelClient esClient() {

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("47.104.190.32:9201")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    /**
     * ElasticsearchRestTemplate无法自定义index的mapping
     */
//    @Bean
//    public RestHighLevelClient client() {
//        ClientConfiguration clientConfiguration
//                = ClientConfiguration.builder()
//                .connectedTo("47.104.190.32:9201")
//                .build();
//
//        return RestClients.create(clientConfiguration).rest();
//    }
//    @Bean
//    public ElasticsearchRestTemplate elasticsearchRestTemplate() {
//
//        return new ElasticsearchRestTemplate(client());
//    }
}
