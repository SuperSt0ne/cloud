package com.stone.common.ding;

import lombok.extern.log4j.Log4j2;

/**
 * 钉钉机器人工具类，发送失败不会抛出异常，仅打印日志
 */
@Log4j2
public class DingTalkUtil {

    public static void send(String robotUrl, String content) {
        dingTalkText(robotUrl, content);
    }

    public static boolean dingTalkText(String robotUrl, String content) {
        return true;
    }

}
