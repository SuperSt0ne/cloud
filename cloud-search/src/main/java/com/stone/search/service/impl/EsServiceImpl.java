package com.stone.search.service.impl;

import com.stone.search.service.EsService;
import org.apache.dubbo.config.annotation.DubboService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class EsServiceImpl implements EsService {

    @Autowired
    private RestHighLevelClient client;


}
