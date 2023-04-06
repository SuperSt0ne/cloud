package com.stone.flink.api.sink;

import com.alibaba.fastjson2.JSON;
import com.stone.flink.api.source.custom.CustomUserOptSource;
import com.stone.sdk.flink.bean.Event;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SinkToMysqlApi {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //读取数据
        DataStreamSource<Event> source = env.addSource(new CustomUserOptSource());
        source.map(data -> {
            System.out.println(JSON.toJSONString(data));
            return data;
        }).addSink(JdbcSink.sink(
                "INSERT INTO `rango`.`user_click_opt` (`user_name`, `url`, `timestamp`) VALUES (?, ?, ?)",
                ((statement, event) -> {
                    statement.setString(1, event.userName);
                    statement.setString(2, event.url);
                    statement.setLong(3, event.timestamp);
                }),
                new JdbcExecutionOptions.Builder()
                        .withBatchSize(5) // 如果不传JdbcExecutionOptions，使用的默认，且batch为5000
                        .build(),
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl("jdbc:mysql://127.0.0.1:3306/rango")
                        .withDriverName("com.mysql.cj.jdbc.Driver")
                        .withUsername("root")
                        .withPassword("123456")
                        .build())
        );
        env.execute();
    }

}
