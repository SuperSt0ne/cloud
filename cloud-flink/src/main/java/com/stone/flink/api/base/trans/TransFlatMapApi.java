package com.stone.flink.api.base.trans;

import com.stone.sdk.flink.bean.Event;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class TransFlatMapApi {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //读取数据
        DataStreamSource<Event> source = env.fromElements(new Event("Rango", "./home", 1000L),
                new Event("Bob", "./home/password", 2000L));

        //转换
        SingleOutputStreamOperator<String> result = source.flatMap((Event event, Collector<String> out) -> {
            out.collect(event.userName);
            out.collect(event.url);
            out.collect(event.timestamp.toString());
        }).returns(TypeInformation.of(String.class));

        result.print();

        env.execute();
    }
}
