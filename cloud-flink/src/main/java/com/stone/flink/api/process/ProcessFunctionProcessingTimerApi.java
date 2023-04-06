package com.stone.flink.api.process;

import com.stone.flink.api.source.custom.CustomUserOptSource;
import com.stone.sdk.flink.bean.Event;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.time.Duration;

/**
 * 底层api 处理函数
 */
public class ProcessFunctionProcessingTimerApi {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Event> stream = env.addSource(new CustomUserOptSource())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                                .withTimestampAssigner(((element, recordTimestamp) -> element.timestamp))
                );

        SingleOutputStreamOperator<String> process = stream.keyBy(data -> data.userName)
                .process(new KeyedProcessFunction<String, Event, String>() {
                    @Override
                    public void processElement(Event value, KeyedProcessFunction<String, Event, String>.Context ctx, Collector<String> out) throws Exception {
                        long curPt = ctx.timerService().currentProcessingTime();
                        out.collect(ctx.getCurrentKey() + " 数据到达时间:" + new Timestamp(curPt));

                        ctx.timerService().registerProcessingTimeTimer(curPt + 5000L);
                    }

                    @Override
                    public void onTimer(long timestamp, KeyedProcessFunction<String, Event, String>.OnTimerContext ctx, Collector<String> out) throws Exception {
                        out.collect(ctx.getCurrentKey() + " 定时器触发, 触发时间: " + new Timestamp(timestamp));
                    }
                });

        process.print();

        env.execute();
    }
}
