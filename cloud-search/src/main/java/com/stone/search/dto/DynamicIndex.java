package com.stone.search.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@Document(indexName = "dynamic_generate_index")
public class DynamicIndex implements Serializable {

    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String title;

}
