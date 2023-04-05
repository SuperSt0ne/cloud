package com.stone.flink.api.agg;

import com.stone.flink.api.source.custom.ClickSource;
import com.stone.sdk.flink.bean.Event;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransReduceApi {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //读取数据
        DataStreamSource<Event> source = env.addSource(new ClickSource());

        //1. 统计每个用户的访问频次
        SingleOutputStreamOperator<Tuple2<String, Long>> clickByUser = source
                .map(event -> Tuple2.of(event.userName, 1L))
                .returns(Types.TUPLE(Types.STRING, Types.LONG))
                .keyBy(data -> data.f0)
                .reduce(((value1, value2) -> Tuple2.of(value1.f0, value1.f1 + value2.f1)));

        //2. 选取最活跃的用户
        SingleOutputStreamOperator<Tuple2<String, Long>> result = clickByUser.keyBy(data -> "key")
                .reduce(((value1, value2) -> value1.f1 > value2.f1 ? value1 : value2));

        result.print();

        env.execute();
    }
}
