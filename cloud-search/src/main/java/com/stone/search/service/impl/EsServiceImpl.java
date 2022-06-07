package com.stone.search.service.impl;

import com.itranswarp.compiler.JavaStringCompiler;
import com.stone.common.exception.GlobalException;
import com.stone.search.service.EsService;
import org.apache.dubbo.config.annotation.DubboService;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@DubboService
public class EsServiceImpl implements EsService {

//    @Autowired
//    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public Boolean existsIndex(String index) {
        return false;
    }

    @Override
    public Boolean dynamicGenerateIndex() throws IOException, ClassNotFoundException {
        String indexName = "test_dynamic_generate_index";

        // 声明类名
        String className = "DynamicIndex";
        String packageName = "com.stone.search.dto";
        // 声明包名：package top.fomeiherz;
        String prefix = String.format("package %s;", packageName);
        // 全类名：top.fomeiherz.Main
        String fullName = String.format("%s.%s", packageName, className);
        String source = "package com.stone.search.dto;\n" +
                "\n" +
                "import lombok.Data;\n" +
                "import org.springframework.data.elasticsearch.annotations.Document;\n" +
                "import org.springframework.data.elasticsearch.annotations.Field;\n" +
                "import org.springframework.data.elasticsearch.annotations.FieldType;\n" +
                "\n" +
                "import java.io.Serializable;\n" +
                "\n" +
                "@Data\n" +
                "@Document(indexName = \"dynamicGenerateIndex\")\n" +
                "public class DynamicIndex implements Serializable {\n" +
                "\n" +
                "    @Field(type = FieldType.Keyword)\n" +
                "    private String title;\n" +
                "\n" +
                "    public DynamicIndex() {\n" +
                "    }\n" +
                "\n" +
                "    public DynamicIndex(String title) {\n" +
                "        this.title = title;\n" +
                "    }\n" +
                "\n" +
                "    public String getTitle() {\n" +
                "        return title;\n" +
                "    }\n" +
                "\n" +
                "    public void setTitle(String title) {\n" +
                "        this.title = title;\n" +
                "    }\n" +
                "}\n";

        // 编译器
        JavaStringCompiler compiler = new JavaStringCompiler();
        // 编译：compiler.compile("Main.java", source)
        Map<String, byte[]> results = compiler.compile(className + ".java", source);
        // 加载内存中byte到Class<?>对象
        Class<?> clazz = compiler.loadClass(fullName, results);

        IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
//        if (!elasticsearchRestTemplate.indexOps(indexCoordinates).exists()) {
//            Document mapping = elasticsearchRestTemplate.indexOps(indexCoordinates).createMapping();
//            mapping = Document.from(getDocument(indexName));
//            Document mapping = elasticsearchRestTemplate.indexOps(indexCoordinates).createMapping(clazz);
//
//            Document setting = Document.from(getDocument(indexName));
//
//            return elasticsearchRestTemplate.indexOps(indexCoordinates).create(setting, mapping);
//        }
        return false;
    }

    private Map<String, Object> getDocument(String indexName) {
        Map<String, Object> keyWord = new HashMap<>();
        keyWord.put("type", "keyWord");
        keyWord.put("ignore_above", 256);

        Map<String, Object> fieldNameMap = new HashMap<>();
        fieldNameMap.put("type", "text");
//        fieldNameMap.put("fields", keyWord);

        Map<String, Object> properties = Collections.singletonMap("title", fieldNameMap);

        Map<String, Object> mappings = Collections.singletonMap("properties", properties);

        Map<String, Object> document = Collections.singletonMap("mappings", mappings);

        return Collections.singletonMap(indexName, document);
    }

    @Override
    public Boolean dynamicIndex() {

        CreateIndexRequest request = new CreateIndexRequest("biz_user_20220525");

        Map<String, Object> id = new HashMap<>();
        id.put("type", "long");

        Map<String, Object> message = new HashMap<>();
        message.put("type", "text");

        Map<String, Object> time = new HashMap<>();
        time.put("type", "date");
        time.put("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd");


        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("id", id);
        properties.put("message", message);
        properties.put("time", time);
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);
        request.mapping(mapping);

        try {
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            return createIndexResponse.isAcknowledged();
        } catch (IOException e) {
            throw new GlobalException(e.getMessage());
        }
    }
}
