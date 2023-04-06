package com.stone.flink.cases.sls;

import com.aliyun.openservices.log.flink.ConfigConstants;
import com.aliyun.openservices.log.flink.FlinkLogConsumer;
import com.aliyun.openservices.log.flink.data.RawLogGroupList;
import com.aliyun.openservices.log.flink.data.RawLogGroupListDeserializer;
import com.aliyun.openservices.log.flink.util.Consts;
import lombok.extern.log4j.Log4j2;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Log4j2
public class SlsLogSimpleConsume {

    private static final String SLS_LOG_ENDPOINT = "";

    private static final String SLS_LOG_ACCESS_KEY_ID = "";

    private static final String SLS_LOG_ACCESS_KEY = "";

    private static final String SLS_LOG_PROJECT = "";

    private static final List<String> LOG_STORE_LIST = new ArrayList<>();

    private static final String SLS_LOG_STORE_UTM_DATA = "";

    static {
        LOG_STORE_LIST.add(SLS_LOG_STORE_UTM_DATA);
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.setInteger(RestOptions.PORT, 8082);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
        env.setParallelism(1);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        env.getCheckpointConfig().setCheckpointTimeout(Duration.ofMinutes(1).toMillis());
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, Time.minutes(1)));
        env.enableCheckpointing(5000);

        Properties configProps = new Properties();
        // 设置访问日志服务的域名。
        configProps.put(ConfigConstants.LOG_ENDPOINT, SLS_LOG_ENDPOINT);
        // 设置用户AccessKey ID及AccessKey Secret。
        configProps.put(ConfigConstants.LOG_ACCESSKEYID, SLS_LOG_ACCESS_KEY_ID);
        configProps.put(ConfigConstants.LOG_ACCESSKEY, SLS_LOG_ACCESS_KEY);
        // 设置日志服务的project。
        configProps.put(ConfigConstants.LOG_PROJECT, SLS_LOG_PROJECT);
        // 设置日志服务的LogStore。
        configProps.put(ConfigConstants.LOG_LOGSTORE, SLS_LOG_STORE_UTM_DATA);
        // 设置消费日志服务起始位置。
        configProps.put(ConfigConstants.LOG_CONSUMER_BEGIN_POSITION, Consts.LOG_END_CURSOR);

        // 设置日志服务的消息反序列化方法。
        RawLogGroupListDeserializer deserializer = new RawLogGroupListDeserializer();

        DataStream<RawLogGroupList> logDataStream = env.addSource(new FlinkLogConsumer<>(SLS_LOG_PROJECT, LOG_STORE_LIST, deserializer, configProps));

        logDataStream.print();

        env.execute();
    }
}
