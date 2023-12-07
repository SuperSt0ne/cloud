package com.stone.common.file;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonthCount {

    public static final Map<String, Integer> SEP_COUNT_MAP = new HashMap<>();

    public static final Map<String, Integer> OCT_COUNT_MAP = new HashMap<>();

    public static final Map<String, Integer> NOV_COUNT_MAP = new HashMap<>();

    static {
        SEP_COUNT_MAP.put(User.LANGE, 435);
        OCT_COUNT_MAP.put(User.LANGE, 681);
        NOV_COUNT_MAP.put(User.LANGE, 988);

        SEP_COUNT_MAP.put(User.XIAOSHU, 300);
        OCT_COUNT_MAP.put(User.XIAOSHU, 576);
        NOV_COUNT_MAP.put(User.XIAOSHU, 826);

        SEP_COUNT_MAP.put(User.NIKA, 197);
        OCT_COUNT_MAP.put(User.NIKA, 389);
        NOV_COUNT_MAP.put(User.NIKA, 581);

        SEP_COUNT_MAP.put(User.LUYAO, 174);
        OCT_COUNT_MAP.put(User.LUYAO, 223);
        NOV_COUNT_MAP.put(User.LUYAO, 264);

        SEP_COUNT_MAP.put(User.XUYUAN, 140);
        OCT_COUNT_MAP.put(User.XUYUAN, 294);
        NOV_COUNT_MAP.put(User.XUYUAN, 295);

        SEP_COUNT_MAP.put(User.KUSHU, 78);
        OCT_COUNT_MAP.put(User.KUSHU, 202);
        NOV_COUNT_MAP.put(User.KUSHU, 292);

        SEP_COUNT_MAP.put(User.ZONGZI, 61);
        OCT_COUNT_MAP.put(User.ZONGZI, 324);
        NOV_COUNT_MAP.put(User.ZONGZI, 533);

        SEP_COUNT_MAP.put(User.CHUANBAI, 55);
        OCT_COUNT_MAP.put(User.CHUANBAI, 95);
        NOV_COUNT_MAP.put(User.CHUANBAI, 158);

        SEP_COUNT_MAP.put(User.JIANGYOU, 41);
        OCT_COUNT_MAP.put(User.JIANGYOU, 62);
        NOV_COUNT_MAP.put(User.JIANGYOU, 143);

        SEP_COUNT_MAP.put(User.NUONUO, 29);
        OCT_COUNT_MAP.put(User.NUONUO, 38);
        NOV_COUNT_MAP.put(User.NUONUO, 41);

        SEP_COUNT_MAP.put(User.WUMING, 18);
        OCT_COUNT_MAP.put(User.WUMING, 21);
        NOV_COUNT_MAP.put(User.WUMING, 22);

        SEP_COUNT_MAP.put(User.YIQI, 0);
        OCT_COUNT_MAP.put(User.YIQI, 0);
        NOV_COUNT_MAP.put(User.YIQI, 140);

        SEP_COUNT_MAP.put(User.XINGKANG, 0);
        OCT_COUNT_MAP.put(User.XINGKANG, 0);
        NOV_COUNT_MAP.put(User.XINGKANG, 70);

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
