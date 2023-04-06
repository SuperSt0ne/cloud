package com.stone.flink.api.base.sink;

import com.alibaba.fastjson2.JSON;
import com.stone.flink.api.base.source.custom.CustomUserOptSource;
import com.stone.sdk.flink.bean.Event;
import com.stone.sdk.flink.constant.PathConstant;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;

import java.util.concurrent.TimeUnit;

public class SinkToFileApi {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //读取数据
        DataStreamSource<Event> source = env.addSource(new CustomUserOptSource(1_000L));

        StreamingFileSink<String> sink = StreamingFileSink.<String>forRowFormat(
                        new Path(PathConstant.OUTPUT_PATH),
                        new SimpleStringEncoder<>("UTF-8"))
                .withRollingPolicy(
                        DefaultRollingPolicy.builder()
                                .withMaxPartSize(1024) //文件大小
                                .withRolloverInterval(TimeUnit.MINUTES.toMillis(1)) //时间间隔
                                .withInactivityInterval(TimeUnit.MILLISECONDS.toMillis(2)) //不活跃
                                .build()
                ) //滚动策略
                .build();

        source.print();
        source.map(JSON::toJSONString).addSink(sink);

        env.execute();

    }
}
