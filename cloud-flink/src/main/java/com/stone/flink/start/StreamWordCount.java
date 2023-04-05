package com.stone.flink.start;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class StreamWordCount {

    public static void main(String[] args) throws Exception {

//        ParameterTool parameter = ParameterTool.fromArgs(args);
//        parameter.get("hostname");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //terminal input cmd: nc -lk 8888
        DataStreamSource<String> dss = env.socketTextStream("127.0.0.1", 8888);

        SingleOutputStreamOperator<Tuple2<String, Long>> wordAndOneTuple = dss.flatMap((String line, Collector<Tuple2<String, Long>> out) -> {
            String[] words = line.split(" ");
            for (String word : words) {
                out.collect(Tuple2.of(word, 1L));
            }
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));

        KeyedStream<Tuple2<String, Long>, String> keyedStream = wordAndOneTuple.keyBy(data -> data.f0);

        SingleOutputStreamOperator<Tuple2<String, Long>> sum = keyedStream.sum(1);

        sum.print();

        env.execute();
    }
}
