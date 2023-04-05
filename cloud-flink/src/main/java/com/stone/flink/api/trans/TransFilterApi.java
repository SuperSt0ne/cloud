package com.stone.flink.api.trans;

import com.stone.sdk.flink.bean.Event;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Objects;

public class TransFilterApi {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //读取数据
        DataStreamSource<Event> source = env.fromElements(new Event("Rango", "./home", 1000L),
                new Event("Bob", "./home/password", 2000L));

        //转换
        SingleOutputStreamOperator<Event> result = source.filter(event -> Objects.equals(event.userName, "Rango"));

        result.print();

        env.execute();
    }
}
