package com.stone.flink.api.base.agg;

import com.stone.sdk.flink.bean.Event;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransKeyByApi {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //读取数据
        DataStreamSource<Event> source = env.fromElements(
                new Event("Rango", "./home", 1000L, 10),
                new Event("Rango", "./prod?id=1", 2000L, 2),
                new Event("Rango", "./cart", 3000L, 5),
                new Event("Rango", "./prod?id=1", 2500L, 20)
        );

        //转换
        source.keyBy(event -> event.userName).max("timestamp").print();
        source.keyBy(event -> event.userName).maxBy("timestamp").print();
        source.keyBy(event -> event.userName).sum("count").print();

        env.execute();
    }
}
