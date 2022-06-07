package com.stone.search.action;

import com.stone.common.result.ApiResult;
import com.stone.search.service.impl.EsServiceImpl;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SearchController {

    @Autowired
    private EsServiceImpl esService;

    @RequestMapping("/existsIndex")
    public ApiResult<Boolean> existsIndex(String index) {
        ApiResult<Boolean> result = new ApiResult<>();
        result.setData(esService.existsIndex(index));
        return result;
    }

    @RequestMapping("/dynamicIndex")
    public ApiResult<Boolean> dynamicGenerateIndex() throws IOException, ClassNotFoundException {
        ApiResult<Boolean> result = new ApiResult<>();
        result.setData(esService.dynamicIndex());
        return result;
    }



}
