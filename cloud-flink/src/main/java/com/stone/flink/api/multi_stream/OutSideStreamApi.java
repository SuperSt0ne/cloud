package com.stone.flink.api.multi_stream;

import com.stone.sdk.flink.source.CustomUserOptSource;
import com.stone.sdk.flink.bean.Event;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Duration;

/**
 * 侧边流
 */
public class OutSideStreamApi {
    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        config.setInteger(RestOptions.PORT, 8888);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);
        env.setParallelism(1);

        DataStreamSource<Event> dataStream = env.addSource(new CustomUserOptSource());

        SingleOutputStreamOperator<Event> stream = dataStream.assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                                .withTimestampAssigner(((element, recordTimestamp) -> element.timestamp))
                );

        //定义输出标签
        OutputTag<Tuple3<String, String, Long>> rangoTag = new OutputTag<Tuple3<String, String, Long>>("Rango") {
        };
        OutputTag<Tuple3<String, String, Long>> bobTag = new OutputTag<Tuple3<String, String, Long>>("Bob") {
        };

        SingleOutputStreamOperator<Event> processedStream = stream.process(new ProcessFunction<Event, Event>() {
            @Override
            public void processElement(Event value, ProcessFunction<Event, Event>.Context ctx, Collector<Event> out) throws Exception {
                if (value.userName.equals("Rango")) {
                    ctx.output(rangoTag, Tuple3.of(value.userName, value.url, value.timestamp));
                } else if (value.userName.equals("Bob")) {
                    ctx.output(bobTag, Tuple3.of(value.userName, value.url, value.timestamp));
                } else {
                    out.collect(value);
                }
            }
        });

        processedStream.print("else");
        processedStream.getSideOutput(rangoTag).print("rango");
        processedStream.getSideOutput(bobTag).print("bob");

        env.execute();

    }
}
