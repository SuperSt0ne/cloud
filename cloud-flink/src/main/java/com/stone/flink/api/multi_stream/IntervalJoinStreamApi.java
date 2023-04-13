package com.stone.flink.api.multi_stream;

import com.stone.sdk.flink.bean.Event;
import com.stone.sdk.flink.bean.Order;
import com.stone.sdk.flink.source.CustomOrderSource;
import com.stone.sdk.flink.source.CustomUserOptSource;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.time.Duration;

public class IntervalJoinStreamApi {

    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        config.setInteger(RestOptions.PORT, 8888);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);
        env.setParallelism(1);

        SingleOutputStreamOperator<Event> eventStream = env.addSource(new CustomUserOptSource())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                                .withTimestampAssigner(((element, recordTimestamp) -> element.timestamp))
                );

        SingleOutputStreamOperator<Order> orderStream = env.addSource(new CustomOrderSource())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Order>forBoundedOutOfOrderness(Duration.ZERO)
                                .withTimestampAssigner(((element, recordTimestamp) -> element.timestamp))
                );

        orderStream.keyBy(order -> order.username)
                .intervalJoin(eventStream.keyBy(event -> event.userName))
                .between(Time.seconds(-5), Time.seconds(10))
                .process(new ProcessJoinFunction<Order, Event, String>() {
                    @Override
                    public void processElement(Order left, Event right, ProcessJoinFunction<Order, Event, String>.Context ctx, Collector<String> out) throws Exception {
                        //简单表示一下：左边的哪些操作导致右边的订单
                        out.collect(right + " => " + left);
                    }
                }).print();

        env.execute();
    }
}
