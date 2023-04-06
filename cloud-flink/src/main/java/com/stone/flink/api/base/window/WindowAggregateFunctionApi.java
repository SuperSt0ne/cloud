package com.stone.flink.api.base.window;

import com.alibaba.fastjson2.JSON;
import com.stone.flink.api.base.source.custom.CustomUserOptSource;
import com.stone.sdk.flink.bean.Event;
import lombok.Data;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.CountTrigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class WindowAggregateFunctionApi {
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();
        configuration.setInteger(RestOptions.PORT, 8082);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
        env.setParallelism(1);

        //读取数据
        DataStreamSource<Event> sourceStream = env.addSource(new CustomUserOptSource());

        SingleOutputStreamOperator<Event> stream = sourceStream.assignTimestampsAndWatermarks(
                WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                        .withTimestampAssigner(((element, recordTimestamp) -> element.timestamp)));

        stream.print("data");

        stream.keyBy(data -> true)
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
//                .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(2))) //滑动窗口
                .trigger(CountTrigger.of(2)) //触发器
                .allowedLateness(Time.minutes(10)) //允许延迟窗口关闭
                .aggregate(new CustomAggregateFunc(), new CustomProcessWindowFunction())
                .print();

        env.execute();
    }

    private static class CustomProcessWindowFunction extends ProcessWindowFunction<StatResult, String, Boolean, TimeWindow> {

        @Override
        public void process(Boolean aBoolean, ProcessWindowFunction<StatResult, String, Boolean, TimeWindow>.Context context,
                            Iterable<StatResult> elements, Collector<String> out) {
            long start = context.window().getStart();
            long end = context.window().getEnd();
            elements.forEach(element -> {
                element.setExecuteTime(context.currentProcessingTime());
                element.setGroup(new Timestamp(start), new Timestamp(end));
                out.collect(JSON.toJSONString(element));
            });
        }
    }

    private static class CustomAggregateFunc implements AggregateFunction<Event, ViewDataAcc, StatResult> {

        @Override
        public ViewDataAcc createAccumulator() {
            ViewDataAcc viewDataAcc = new ViewDataAcc();
            viewDataAcc.setUvMap(new HashMap<>());
            viewDataAcc.setPv(0L);
            return viewDataAcc;
        }

        @Override
        public ViewDataAcc add(Event value, ViewDataAcc accumulator) {
            accumulator.pv = accumulator.pv + 1;
            Map<String, Long> uvMap = accumulator.uvMap;
            if (uvMap.containsKey(value.userName)) {
                uvMap.computeIfPresent(value.userName, (k, v) -> v + 1);
            } else {
                uvMap.put(value.userName, 1L);
            }
            return accumulator;
        }

        @Override
        public StatResult getResult(ViewDataAcc accumulator) {
            return new StatResult(accumulator.pv, accumulator.uvMap);
        }

        @Override
        public ViewDataAcc merge(ViewDataAcc a, ViewDataAcc b) {
            return null;
        }
    }

    @Data
    private static class StatResult {
        private Long pv;

        private Map<String, Long> uvMap;

        private String group;

        private Long executeTime;

        public StatResult(Long pv, Map<String, Long> uvMap) {
            this.pv = pv;
            this.uvMap = uvMap;
        }

        public void setGroup(Timestamp start, Timestamp end) {
            this.group = start + " ~ " + end;
        }
    }

    @Data
    private static class ViewDataAcc {

        private Long pv;

        private Map<String, Long> uvMap;

    }
}
