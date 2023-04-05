package com.stone.flink.api.window;

import com.stone.flink.api.source.custom.ClickSource;
import com.stone.sdk.flink.bean.Event;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.time.Duration;

public class WindowReduceFunctionApi {
    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.setInteger(RestOptions.PORT, 8082);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
        env.setParallelism(1);

        //读取数据
        DataStreamSource<Event> sourceStream = env.addSource(new ClickSource());

        SingleOutputStreamOperator<Event> stream = sourceStream.assignTimestampsAndWatermarks(
                WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(1))
                        .withTimestampAssigner(((element, recordTimestamp) -> element.timestamp)));

        stream.map((MapFunction<Event, Tuple2<String, Long>>) value -> Tuple2.of(value.userName, 1L))
                .returns(Types.TUPLE(Types.STRING, Types.LONG))
                .keyBy(data -> data.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(5))) //滚动窗口
//                .window(SlidingEventTimeWindows.of(Time.seconds(30), Time.seconds(5))); //滑动窗口
//                .window(EventTimeSessionWindows.withGap(Time.seconds(2))); //事件会话窗口
//                .countWindow(10); //滚动计数窗口
                .reduce((ReduceFunction<Tuple2<String, Long>>) (value1, value2) -> Tuple2.of(value1.f0, value1.f1 + value2.f1)).print();


        env.execute();
    }
}
