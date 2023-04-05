package com.stone.sdk.flink.bean;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Event {

    public String userName;

    public String url;

    public Long timestamp;

    public Integer count;

    public Event() {
    }

    public Event(String userName, String url, Long timestamp) {
        this.userName = userName;
        this.url = url;
        this.timestamp = timestamp;
    }

    public Event(String userName, String url, Long timestamp, Integer count) {
        this.userName = userName;
        this.url = url;
        this.timestamp = timestamp;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Event{" +
                "userName='" + userName + '\'' +
                ", url='" + url + '\'' +
                ", timestamp='" + new Timestamp(timestamp) + '\'' +
//                ", count='" + count + '\'' +
                '}';
    }
}
