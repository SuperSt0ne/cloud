package com.stone.flink.cases.topn;

import com.stone.flink.api.base.source.custom.CustomUserOptSource;
import com.stone.sdk.flink.bean.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopNCase_ProcessAllWindowFunction {

    public static final int TOP_N = 5;

    public static void main(String[] args) throws Exception {

        Configuration config = new Configuration();
        config.setInteger(RestOptions.PORT, 8888);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);
        env.setParallelism(1);

        SingleOutputStreamOperator<Event> stream = env.addSource(new CustomUserOptSource(500L))
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                        .withTimestampAssigner((SerializableTimestampAssigner<Event>) (element, recordTimestamp) -> element.timestamp));

//        stream.print("click");

        stream.map(data -> data.url)
                .windowAll(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                .aggregate(new UrlHashMapAgg(), new UrlAllWindowResult())
                .print();


        //对于同一窗口统计出的访问量,进行收集和排序

        env.execute();
    }

    public static class UrlHashMapAgg implements AggregateFunction<String, Map<String, Integer>, List<Tuple2<String, Integer>>> {

        @Override
        public Map<String, Integer> createAccumulator() {
            return new HashMap<>();
        }

        @Override
        public Map<String, Integer> add(String url, Map<String, Integer> accumulator) {
            Integer count = accumulator.getOrDefault(url, 0);
            accumulator.put(url, count + 1);
            return accumulator;
        }

        @Override
        public List<Tuple2<String, Integer>> getResult(Map<String, Integer> accumulator) {
            List<Tuple2<String, Integer>> result = new ArrayList<>();
            accumulator.forEach((k, v) -> result.add(Tuple2.of(k, v)));
            result.sort((o1, o2) -> o2.f1 - o1.f1);
            return result;
        }

        @Override
        public Map<String, Integer> merge(Map<String, Integer> a, Map<String, Integer> b) {
            return null;
        }
    }

    public static class UrlAllWindowResult extends ProcessAllWindowFunction<List<Tuple2<String, Integer>>, String, TimeWindow> {

        @Override
        public void process(ProcessAllWindowFunction<List<Tuple2<String, Integer>>, String, TimeWindow>.Context context,
                            Iterable<List<Tuple2<String, Integer>>> elements, Collector<String> out) throws Exception {
            StringBuilder builder = new StringBuilder("\n");
            builder.append("-------------------------\n");
            builder.append("window start:").append(new Timestamp(context.window().getStart())).append(" ");
            builder.append("window end:").append(new Timestamp(context.window().getEnd())).append("\n");

            List<Tuple2<String, Integer>> data = elements.iterator().next();
            for (int i = 0; i < (Math.min(data.size(), TOP_N)); i++) {
                Tuple2<String, Integer> curTuple = data.get(i);
                builder.append("No.").append(i + 1).append(" ");
                builder.append("url:").append(curTuple.f0).append(" ");
                builder.append("view count:").append(curTuple.f1).append(" \n");
            }
            builder.append("-------------------------");

            out.collect(builder.toString());
        }
    }

}
