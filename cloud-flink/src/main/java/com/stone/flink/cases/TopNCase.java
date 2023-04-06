package com.stone.flink.cases;

import com.stone.flink.api.source.custom.CustomUserOptSource;
import com.stone.sdk.flink.bean.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TopNCase {

    public static void main(String[] args) throws Exception {

        Configuration config = new Configuration();
        config.setInteger(RestOptions.PORT, 8888);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);
//        env.setParallelism(1);

        SingleOutputStreamOperator<Event> stream = env.addSource(new CustomUserOptSource(1000L))
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                        .withTimestampAssigner((SerializableTimestampAssigner<Event>) (element, recordTimestamp) -> element.timestamp));

//        stream.print("click");

        SingleOutputStreamOperator<UrlViewCount> urlViewCountStream = stream.keyBy(data -> data.url)
                .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                .aggregate(new UrlViewCountAgg(), new UrlViewCountResult());

        urlViewCountStream.print("url view count");

        //对于同一窗口统计出的访问量,进行收集和排序
        urlViewCountStream.keyBy(data -> data.end)
                .process(new TopNProcessResult(5))
                .print();

        env.execute();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class TopNProcessResult extends KeyedProcessFunction<Long, UrlViewCount, String> {

        private Integer count;

        private ListState<UrlViewCount> urlViewCountListState;

        public TopNProcessResult(Integer count) {
            this.count = count;
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            urlViewCountListState = getRuntimeContext().getListState(
                    new ListStateDescriptor<>("url-count-list", Types.POJO(UrlViewCount.class))
            );
        }

        @Override
        public void processElement(UrlViewCount value, KeyedProcessFunction<Long, UrlViewCount, String>.Context ctx,
                                   Collector<String> out) throws Exception {
            //将数据保存到状态中
            urlViewCountListState.add(value);
            //注册window end + 1ms的定时器
            ctx.timerService().registerEventTimeTimer(ctx.getCurrentKey() + 1);
        }

        @Override
        public void onTimer(long timestamp, KeyedProcessFunction<Long, UrlViewCount, String>.OnTimerContext ctx,
                            Collector<String> out) throws Exception {
            List<UrlViewCount> urlViewCounts = new ArrayList<>();
            urlViewCountListState.get().forEach(urlViewCounts::add);
            //排序
            urlViewCounts.sort((o1, o2) -> o2.count.intValue() - o1.count.intValue());

            StringBuilder builder = new StringBuilder("\n");
            builder.append("-------------------------\n");
            builder.append("window end:").append(new Timestamp(ctx.getCurrentKey())).append("\n");

            for (int i = 0; i < (Math.min(urlViewCounts.size(), count)); i++) {
                UrlViewCount curUrlViewCount = urlViewCounts.get(i);
                builder.append("No.").append(i + 1).append(" ");
                builder.append("url:").append(curUrlViewCount.url).append(" ");
                builder.append("view count:").append(curUrlViewCount.count).append(" \n");
            }
            builder.append("-------------------------");
            out.collect(builder.toString());
        }
    }

    public static class UrlViewCountAgg implements AggregateFunction<Event, Long, Long> {

        @Override
        public Long createAccumulator() {
            return 0L;
        }

        @Override
        public Long add(Event value, Long accumulator) {
            return accumulator + 1;
        }

        @Override
        public Long getResult(Long accumulator) {
            return accumulator;
        }

        @Override
        public Long merge(Long a, Long b) {
            return null;
        }
    }

    public static class UrlViewCountResult extends ProcessWindowFunction<Long, UrlViewCount, String, TimeWindow> {

        @Override
        public void process(String url, ProcessWindowFunction<Long, UrlViewCount, String, TimeWindow>.Context context,
                            Iterable<Long> elements, Collector<UrlViewCount> out) throws Exception {
            TimeWindow window = context.window();
            out.collect(new UrlViewCount(url, elements.iterator().next(), window.getStart(), window.getEnd()));
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UrlViewCount {
        public String url;

        public Long count;

        public Long start;

        public Long end;

        @Override
        public String toString() {
            return "UrlViewCount{" +
                    "url='" + url + '\'' +
                    ", count=" + count +
                    ", start=" + new Timestamp(start) +
                    ", end=" + new Timestamp(end) +
                    '}';
        }
    }


}
