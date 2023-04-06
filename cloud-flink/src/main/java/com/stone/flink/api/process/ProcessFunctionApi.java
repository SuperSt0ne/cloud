package com.stone.flink.api.process;

import com.stone.flink.api.source.custom.CustomUserOptSource;
import com.stone.sdk.flink.bean.Event;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.Objects;

/**
 * 底层api 处理函数
 */
public class ProcessFunctionApi {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Event> stream = env.addSource(new CustomUserOptSource())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                                .withTimestampAssigner(((element, recordTimestamp) -> element.timestamp))
                );

        SingleOutputStreamOperator<String> process = stream.process(new ProcessFunction<Event, String>() {
            @Override
            public void processElement(Event value, ProcessFunction<Event, String>.Context ctx, Collector<String> out) {
                if (Objects.equals(value.userName, "Rango")) {
                    out.collect(value.userName + " hard click " + value.url);
                } else {
                    out.collect(value.userName + " soft click " + value.url);
                }
                System.out.println("timestamp:" + ctx.timestamp());
                TimerService timerService = ctx.timerService();
                System.out.println("current watermark:" + timerService.currentWatermark());

                RuntimeContext runtimeContext = getRuntimeContext();
                System.out.println("runtime context:" + runtimeContext.getTaskName());
            }

            @Override
            public void onTimer(long timestamp, ProcessFunction<Event, String>.OnTimerContext ctx, Collector<String> out) throws Exception {
                super.onTimer(timestamp, ctx, out);
            }
        });

        process.print();

        env.execute();
    }
}
