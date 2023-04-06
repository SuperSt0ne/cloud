package com.stone.flink.api.base.source;

import com.stone.sdk.flink.bean.Event;
import com.stone.sdk.flink.constant.PathConstant;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

public class SourceNormal {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //1. 从文件中读取
        DataStreamSource<String> source_1 = env.readTextFile(PathConstant.INPUT_PATH + "clicks.txt");

        //2. 从集合中读取
        List<Integer> numList = new ArrayList<>();
        numList.add(1);
        numList.add(2);
        DataStreamSource<Integer> source_2 = env.fromCollection(numList);

        List<Event> events = new ArrayList<>();
        events.add(new Event("Rango", "./home", 1000L));
        events.add(new Event("Bob", "./home/password", 2000L));
        DataStreamSource<Event> source_3 = env.fromCollection(events);

        //3. 从元素读取数据
        DataStreamSource<Event> source_4 = env.fromElements(new Event("Rango", "./home", 1000L),
                new Event("Bob", "./home/password", 2000L));

        //4. 从socket文本流中读取 nc -lk 8888
        DataStreamSource<String> source_5 = env.socketTextStream("127.0.0.1", 8888);

        source_1.print();
        source_2.print();
        source_3.print();
        source_4.print();
        source_5.print();

        env.execute();
    }
}
