package com.stone.flink.api.multi_stream;

import com.stone.sdk.flink.bean.Event;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * 合流
 * 需要多条流的数据类型相同
 */
public class UnionStreamApi {
    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        config.setInteger(RestOptions.PORT, 8888);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);
        env.setParallelism(1);

        //nc -lk 7777
        SingleOutputStreamOperator<Event> dataStream1 = env.socketTextStream("127.0.0.1", 7777)
                .map(data -> {
                    String[] field = data.split(",");
                    return new Event(field[0], field[1], Long.parseLong(field[2]));
                }).assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(2L))
                                .withTimestampAssigner(((element, recordTimestamp) -> element.timestamp))
                );

        dataStream1.print("stream-1");

        //nc -lk 7778
        SingleOutputStreamOperator<Event> dataStream2 = env.socketTextStream("127.0.0.1", 7778)
                .map(data -> {
                    String[] field = data.split(",");
                    return new Event(field[0], field[1], Long.parseLong(field[2]));
                }).assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(5L))
                                .withTimestampAssigner(((element, recordTimestamp) -> element.timestamp))
                );

        dataStream2.print("stream-2");

        DataStream<Event> unionStream = dataStream1.union(dataStream2);

        unionStream.process(new ProcessFunction<Event, String>() {
            @Override
            public void processElement(Event value, ProcessFunction<Event, String>.Context ctx, Collector<String> out) throws Exception {
                out.collect("水位线: " + ctx.timerService().currentWatermark());
            }
        }).print();

        env.execute();

    }
}
