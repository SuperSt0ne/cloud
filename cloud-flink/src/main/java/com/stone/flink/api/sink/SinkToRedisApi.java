package com.stone.flink.api.sink;

import com.stone.flink.api.source.custom.ClickSource;
import com.stone.sdk.flink.bean.Event;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

public class SinkToRedisApi {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //读取数据
        DataStreamSource<Event> source = env.addSource(new ClickSource());

        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder()
                .setHost("127.0.0.1")
                .build();

        RedisSink<Event> sink = new RedisSink<>(conf, new MyRedisMapper());

        source.addSink(sink);

        env.execute();

    }

    public static class MyRedisMapper implements RedisMapper<Event> {

        @Override
        public RedisCommandDescription getCommandDescription() {
            return new RedisCommandDescription(RedisCommand.HSET, "flink_user_last_opt");
        }

        @Override
        public String getKeyFromData(Event event) {
            return event.userName;
        }

        @Override
        public String getValueFromData(Event event) {
            return event.url;
        }
    }
}
