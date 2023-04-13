package com.stone.sdk.flink.source;

import com.stone.sdk.flink.bean.Event;
import com.stone.sdk.flink.bean.Order;
import lombok.Data;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 自定义SourceFunction
 */
@Data
public class CustomOrderSource implements SourceFunction<Order> {

    //表示位
    public Boolean running = true;

    public Integer sleepTime;

    @Override
    public void run(SourceContext<Order> sourceContext) throws Exception {
        //随机生成数据
        Random random = new Random();
        //定义字段选取的数据集
        String[] users = {"Rango", "Bob", "Mary", "Tim"};

        while (running) {
            String user = users[random.nextInt(users.length)];
            long goodsId = (long) (Math.random() * 100);
            long amount = (long) (Math.random() * 1000);
            sourceContext.collect(new Order(goodsId, amount, user, System.currentTimeMillis()));
            if (sleepTime > 0) {
                TimeUnit.SECONDS.sleep(sleepTime);
            }
        }
    }

    @Override
    public void cancel() {
        running = false;
    }

    public CustomOrderSource() {
        this.sleepTime = 2;
    }

    public CustomOrderSource(Integer sleepTime) {
        this.sleepTime = sleepTime;
    }

}
