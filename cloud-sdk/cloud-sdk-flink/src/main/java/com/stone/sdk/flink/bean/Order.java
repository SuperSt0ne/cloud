package com.stone.sdk.flink.bean;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Order {

    public static void main(String[] args) {
        int a = 10;
        if (++a > 10) {
            System.out.println(a);
        }
    }

    public Long goodsId;

    public Long mount;

    public String username;

    public Long timestamp;

    public Order() {
    }

    public Order(Long goodsId, Long mount, String username, Long timestamp) {
        this.goodsId = goodsId;
        this.mount = mount;
        this.username = username;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Order{" +
                "goodsId=" + goodsId +
                ", mount=" + mount +
                ", username='" + username + '\'' +
                ", timestamp='" + new Timestamp(timestamp) + '\'' +
                '}';
    }
}
