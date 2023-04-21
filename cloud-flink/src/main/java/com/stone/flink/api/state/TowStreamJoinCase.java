package com.stone.flink.api.state;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;

/**
 * ListState
 */
public class TowStreamJoinCase {

    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        config.setInteger(RestOptions.PORT, 8888);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(config);
        env.setParallelism(1);

        SingleOutputStreamOperator<Tuple3<String, String, Long>> stream_1 = env.fromElements(
                Tuple3.of("a", "stream-1", 1000L),
                Tuple3.of("a", "stream-1", 2000L),
                Tuple3.of("b", "stream-1", 2000L),
                Tuple3.of("a", "stream-1", 3000L)
        ).assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, String, Long>>forMonotonousTimestamps()
                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                    @Override
                    public long extractTimestamp(Tuple3<String, String, Long> element, long recordTimestamp) {
                        return element.f2;
                    }
                }));

        SingleOutputStreamOperator<Tuple3<String, String, Long>> stream_2 = env.fromElements(
                Tuple3.of("a", "stream-2", 3000L),
                Tuple3.of("a", "stream-2", 4000L),
                Tuple3.of("b", "stream-2", 4000L)
        ).assignTimestampsAndWatermarks(WatermarkStrategy.<Tuple3<String, String, Long>>forMonotonousTimestamps()
                .withTimestampAssigner(new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
                    @Override
                    public long extractTimestamp(Tuple3<String, String, Long> element, long recordTimestamp) {
                        return element.f2;
                    }
                }));

        //自定义列表状态进行全外连接
//        stream_1.connect(stream_2)
//                .keyBy(data -> data.f0, data -> data.f0)
//                .process(new CoProcessFunction<Tuple3<String, String, Long>, Tuple3<String, String, Long>, String>() {
//
//                    //定义列表状态，用于保存两条流中已经达到的所有数据
//                    private ListState<Tuple2<String, Long>> listState_1;
//                    private ListState<Tuple2<String, Long>> listState_2;
//
//                    @Override
//                    public void processElement1(Tuple3<String, String, Long> left, Context ctx, Collector<String> out) throws Exception {
//                        for (Tuple2<String, Long> right : listState_2.get()) {
//                            out.collect(left.f0 + " " + left.f2 + " => " + right);
//                        }
//                        listState_1.add(Tuple2.of(left.f0, left.f2));
//                    }
//
//                    @Override
//                    public void processElement2(Tuple3<String, String, Long> right, Context ctx, Collector<String> out) throws Exception {
//                        for (Tuple2<String, Long> left : listState_2.get()) {
//                            out.collect(left + " => " + right.f0 + " " + right.f2);
//                        }
//                        listState_2.add(Tuple2.of(right.f0, right.f2));
//                    }
//
//                    @Override
//                    public void open(Configuration parameters) throws Exception {
//                        listState_1 = getRuntimeContext().getListState(new ListStateDescriptor<>("list-1", Types.TUPLE(Types.STRING, Types.LONG)));
//                        listState_2 = getRuntimeContext().getListState(new ListStateDescriptor<>("list-2", Types.TUPLE(Types.STRING, Types.LONG)));
//                    }
//                }).print();
        stream_1.keyBy(data -> data.f0)
                .connect(stream_2.keyBy(data -> data.f0))
                .process(new CoProcessFunction<Tuple3<String, String, Long>, Tuple3<String, String, Long>, String>() {
                    //定义列表状态，用于保存两条流中已经达到的所有数据
                    private ListState<Tuple2<String, Long>> listState_1;
                    private ListState<Tuple2<String, Long>> listState_2;

                    @Override
                    public void processElement1(Tuple3<String, String, Long> left, CoProcessFunction<Tuple3<String, String, Long>, Tuple3<String, String, Long>, String>.Context ctx, Collector<String> out) throws Exception {
                        listState_1.add(Tuple2.of(left.f0, left.f2));
                        for (Tuple2<String, Long> right : listState_2.get()) {
                            out.collect(left.f0 + " " + left.f2 + " => " + right);
                        }
                    }

                    @Override
                    public void processElement2(Tuple3<String, String, Long> right, CoProcessFunction<Tuple3<String, String, Long>, Tuple3<String, String, Long>, String>.Context ctx, Collector<String> out) throws Exception {
                        listState_2.add(Tuple2.of(right.f0, right.f2));
                        for (Tuple2<String, Long> left : listState_2.get()) {
                            out.collect(left + " => " + right.f0 + " " + right.f2);
                        }
                    }

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        listState_1 = getRuntimeContext().getListState(new ListStateDescriptor<>("list-1", Types.TUPLE(Types.STRING, Types.LONG)));
                        listState_2 = getRuntimeContext().getListState(new ListStateDescriptor<>("list-2", Types.TUPLE(Types.STRING, Types.LONG)));
                    }
                }).print();

        env.execute();
    }

}
