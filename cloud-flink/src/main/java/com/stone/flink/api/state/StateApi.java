package com.stone.flink.api.state;

import com.stone.sdk.flink.bean.Event;
import com.stone.sdk.flink.source.CustomUserOptSource;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class StateApi {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Event> dataSource = env.addSource(new CustomUserOptSource());

        dataSource.keyBy(event -> event.userName)
                .flatMap(new CustomFlatMapFunction())
                .print();

        env.execute();
    }

    private static class CustomFlatMapFunction extends RichFlatMapFunction<Event, String> {

        private ValueState<Event> valueState;

        // TODO: 2023/4/20 尝试其他状态设置

        @Override
        public void flatMap(Event value, Collector<String> out) throws Exception {
            System.out.println(valueState.value());
            valueState.update(value);
            out.collect(value.toString());
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            valueState = getRuntimeContext().getState(new ValueStateDescriptor<>("value-sate", Event.class));

        }
    }
}
