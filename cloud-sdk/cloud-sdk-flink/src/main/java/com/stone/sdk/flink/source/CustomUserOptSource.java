package com.stone.sdk.flink.source;

import com.stone.sdk.flink.bean.Event;
import lombok.Data;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 自定义SourceFunction
 */
@Data
public class CustomUserOptSource implements SourceFunction<Event> {

    //表示位
    public Boolean running = true;

    public Integer sleepTime;

    @Override
    public void run(SourceContext<Event> sourceContext) throws Exception {
        //随机生成数据
        Random random = new Random();
        //定义字段选取的数据集
        String[] users = {"Rango", "Bob", "Mary", "Tim"};
        String[] urls = {"./home", "./prod", "./fav", "./cart", "./chat"};

        while (running) {
            String user = users[random.nextInt(users.length)];
            String url = urls[random.nextInt(urls.length)] + "?id=" + (int) (Math.random() * 10);
//            sourceContext.collect(new Event(user, url, Calendar.getInstance().getTimeInMillis(), (int) (Math.random() * 100)));
            sourceContext.collect(new Event(user, url, System.currentTimeMillis()));
            if (sleepTime > 0) {
                TimeUnit.SECONDS.sleep(sleepTime);
            }
        }
    }

    @Override
    public void cancel() {
        running = false;
    }

    public CustomUserOptSource() {
        this.sleepTime = 1;
    }

    public CustomUserOptSource(Integer sleepTime) {
        this.sleepTime = sleepTime;
    }

}
