package com.stone.flink.api.base.partition;

import com.stone.sdk.flink.source.CustomUserOptSource;
import com.stone.sdk.flink.bean.Event;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransPartitionApi {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //读取数据
        DataStreamSource<Event> source = env.addSource(new CustomUserOptSource());

        //1.随机分区
        source.shuffle().print().setParallelism(4);
        //2.轮询分区
        source.rebalance().print().setParallelism(4);
        //3.重缩放分区
        source.rescale().print().setParallelism(4);
        //4.广播分区
        source.broadcast().print().setParallelism(4);
        //5.全局分区 合并了数据，全局放在了一个分区，设置并行度没有用了
        source.global().print().setParallelism(4);
        //6.自定义分区

        env.execute();
    }
}
