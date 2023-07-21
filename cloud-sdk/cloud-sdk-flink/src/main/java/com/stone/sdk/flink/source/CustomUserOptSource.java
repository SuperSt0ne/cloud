package com.stone.sdk.flink.source;

import com.stone.sdk.flink.bean.Event;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 自定义SourceFunction
 */
@Data
public class CustomUserOptSource implements SourceFunction<Event> {

    public static Map<String, String> getBaseOptContentMap() {
        Map<String, String> map = new HashMap<>();
        map.put("系统商品名称", null);
        map.put("系统规格名称", null);
        map.put("分销商商品名称", null);
        map.put("分销商规格名称", null);
        return map;
    }

    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder();
        Map<String, String> map = getBaseOptContentMap();
        map.put("分销商商品名称", "a --> b");
        map.put("分销商规格名称", "c --> d");
        map.forEach((k, v) -> {
            if (StringUtils.isBlank(v)) return;
            builder.append("\n").append(k).append(" : ").append(v);
        });
        System.out.println(builder.toString().length() == 0 ? null : builder.substring(2));
    }

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
