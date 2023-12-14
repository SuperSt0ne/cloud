package com.stone.flink.api.base.trans;

import com.stone.sdk.flink.bean.Event;
import com.stone.sdk.flink.source.CustomUserOptSource;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * flatMap 是 Flink 中的一个转换操作，用于将一个数据集的元素拆分成多个元素，并将它们输出到新的数据集中。
 * 它与 map 操作相似，但 map 操作将每个元素转换为一个新的元素，而 flatMap 操作将每个元素转换为零个或多个元素。
 */
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
