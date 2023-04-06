package com.stone.flink.api.base.udf;

import com.stone.sdk.flink.bean.Event;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransUDFRichApi {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //读取数据
        DataStreamSource<Event> source = env.fromElements(
                new Event("Rango", "./home", 1000L, 10),
                new Event("Rango", "./prod?id=1", 2000L, 2),
                new Event("Rango", "./cart", 3000L, 5)
        );

        source.map(new MyRichFunction()).setParallelism(2).print();

        env.execute();
    }

    public static class MyRichFunction extends RichMapFunction<Event, String> {

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            System.out.println("open生命周期被调用:" + getRuntimeContext().getIndexOfThisSubtask() + "号任务启动");
        }

        @Override
        public String map(Event value) {
            return getRuntimeContext().getIndexOfThisSubtask() + "号任务处理:" + value.userName;
        }

        @Override
        public void close() throws Exception {
            super.close();
            System.out.println("close生命周期被调用:" + getRuntimeContext().getIndexOfThisSubtask() + "号任务启动");
        }
    }
}
