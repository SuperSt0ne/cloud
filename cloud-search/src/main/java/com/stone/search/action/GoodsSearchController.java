package com.stone.search.action;

import com.stone.common.result.ApiResult;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GoodsSearchController {

    @Autowired
    private RestHighLevelClient client;

    @RequestMapping("/index/create")
    public ApiResult<CreateIndexResponse> createGoodsIndex(String index) throws IOException {
        ApiResult<CreateIndexResponse> result = new ApiResult<>();
        CreateIndexRequest request = new CreateIndexRequest(index);
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        result.setData(response);
        return result;
    }

    @RequestMapping("/index/search")
    public ApiResult<Boolean> getGoodsIndex(String index) throws IOException {
        ApiResult<Boolean> result = new ApiResult<>();
        GetIndexRequest request = new GetIndexRequest(index);
        result.setData(client.indices().exists(request, RequestOptions.DEFAULT));
        return result;
    }
}
