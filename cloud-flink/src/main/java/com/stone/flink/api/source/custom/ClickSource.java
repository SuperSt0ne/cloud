package com.stone.flink.api.source.custom;

import com.stone.sdk.flink.bean.Event;
import lombok.Data;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 自定义SourceFunction
 */
@Data
public class ClickSource implements SourceFunction<Event> {

    //表示位
    private Boolean running = true;

    private Long sleepTime = 1000L;

    @Override
    public void run(SourceContext<Event> sourceContext) throws Exception {
        //随机生成数据
        Random random = new Random();
        //定义字段选取的数据集
        String[] users = {"Rango", "Bob", "Mary"};
        String[] urls = {"./home", "./prod", "./fav", "./cart", "./chat"};

        while (running) {
            String user = users[random.nextInt(users.length)];
            String url = urls[random.nextInt(urls.length)] + "?id=" + (int) (Math.random() * 10);
//            sourceContext.collect(new Event(user, url, Calendar.getInstance().getTimeInMillis(), (int) (Math.random() * 100)));
            sourceContext.collect(new Event(user, url, System.currentTimeMillis()));
            if (sleepTime > 0) {
                TimeUnit.MICROSECONDS.sleep(sleepTime);
            }
        }
    }

    @Override
    public void cancel() {
        running = false;
    }

    public ClickSource() {
    }

    public ClickSource(Long sleepTime) {
        this.sleepTime = sleepTime;
    }

}
