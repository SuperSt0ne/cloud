package com.stone.common.file;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonthCount {

    public static final Map<String, Integer> SEP_COUNT_MAP = new HashMap<>();

    public static final Map<String, Integer> OCT_COUNT_MAP = new HashMap<>();

    static {
        SEP_COUNT_MAP.put(User.LANGE, 435);
        OCT_COUNT_MAP.put(User.LANGE, 681);

        SEP_COUNT_MAP.put(User.XIAOSHU, 300);
        OCT_COUNT_MAP.put(User.XIAOSHU, 576);

        SEP_COUNT_MAP.put(User.NIKA, 197);
        OCT_COUNT_MAP.put(User.NIKA, 389);

        SEP_COUNT_MAP.put(User.LUYAO, 174);
        OCT_COUNT_MAP.put(User.LUYAO, 223);

        SEP_COUNT_MAP.put(User.XUYUAN, 140);
        OCT_COUNT_MAP.put(User.XUYUAN, 294);

        SEP_COUNT_MAP.put(User.KUSHU, 78);
        OCT_COUNT_MAP.put(User.KUSHU, 202);

        SEP_COUNT_MAP.put(User.ZONGZI, 61);
        OCT_COUNT_MAP.put(User.ZONGZI, 324);

        SEP_COUNT_MAP.put(User.CHUANBAI, 55);
        OCT_COUNT_MAP.put(User.CHUANBAI, 95);

        SEP_COUNT_MAP.put(User.JIANGYOU, 41);
        OCT_COUNT_MAP.put(User.JIANGYOU, 62);

        SEP_COUNT_MAP.put(User.NUONUO, 29);
        OCT_COUNT_MAP.put(User.NUONUO, 38);

        SEP_COUNT_MAP.put(User.WUMING, 18);
        OCT_COUNT_MAP.put(User.WUMING, 21);
    }

    public static void main(String[] args) {
        String str = "//    @BizStep(value = \"[应用配置项]限流规则改变\", developer = Developer.LANGE, uniqueCode = \"20231024171420_559697\")";
        Pattern pattern = Pattern.compile("@(.*?)\\((.*)\\)");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String group = matcher.group();
            System.out.println(group.substring(0, group.indexOf("(")));
            System.out.println(group.substring(group.indexOf("(") + 1, group.indexOf(")")));
        }
    }
}
