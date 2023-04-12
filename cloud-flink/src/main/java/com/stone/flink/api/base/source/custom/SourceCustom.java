package com.stone.flink.api.base.source.custom;

import com.stone.sdk.flink.bean.Event;
import com.stone.sdk.flink.source.CustomUserOptSource;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SourceCustom {
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();
        configuration.setInteger(RestOptions.PORT, 8082);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
        env.setParallelism(1);

        DataStreamSource<Event> customSource = env.addSource(new CustomUserOptSource());

        customSource.print();

        env.execute("click-test");
    }
}
