package com.stone.search.service;

import org.springframework.data.elasticsearch.core.IndexOperations;

import java.io.IOException;

public interface EsService {

    Boolean existsIndex(String index);

    Boolean dynamicGenerateIndex() throws IOException, ClassNotFoundException;


    Boolean dynamicIndex();

}
