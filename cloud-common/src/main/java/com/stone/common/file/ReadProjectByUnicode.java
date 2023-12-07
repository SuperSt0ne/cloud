package com.stone.common.file;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadProjectByUnicode {

    private static final Map<String, String> USER_MAP = new HashMap<>();

    private static final Map<String, Map<String, Integer>> MONTH_USER_COUNT_MAP = new LinkedHashMap<>();

    private static final Map<String, Integer> USER_COUNT_MAP = new HashMap<>();

    public static List<String> UNI_CONTENT_LIST = new ArrayList<>();

    public static List<String> CUR_MONTH_DETAIL = new ArrayList<>();

    private static final String PREFIX_12 = "202312";
    private static final String PREFIX_11 = "202311";
    private static final String PREFIX_10 = "202310";
    private static final String PREFIX_09 = "202309";

    private static final List<String> ANNOTATIONS = Arrays.asList("@BizBuild", "@BizCondition", "@BizConvert",
            "@BizFolder", "@BizMethod", "@BizPhase", "@BizRespository", "@BizService", "@BizStep", "@BizValidation");

    private static final Pattern PATTERN = Pattern.compile("@(.*?)\\((.*)\\)");

    private static final String PATH_MAC = "/Users/stone/IdeaProjects/yt/slt";

    private static final String PATH_MAC_MINI = "/Users/stone/code/yt/slt";

    static {
        MONTH_USER_COUNT_MAP.put("202312", new HashMap<>());
        MONTH_USER_COUNT_MAP.put("202311", new HashMap<>());
        MONTH_USER_COUNT_MAP.put("202310", new HashMap<>());
        MONTH_USER_COUNT_MAP.put("202309", new HashMap<>());
        try {
            parseUser();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File(PATH_MAC);
        ReadProject.search(file);
        UNI_CONTENT_LIST = ReadProject.CONTENT_LIST;

        search(file);
        printDetail();
        printUserCountMap();
        printLatestIncrement();
        printMonthUserCountMap();
    }

    private static void printDetail() {
        CUR_MONTH_DETAIL.forEach(System.out::println);
    }

    private static void printUserCountMap() {
        Map<String, Integer> result = Maps.newLinkedHashMapWithExpectedSize(USER_COUNT_MAP.size());
        USER_COUNT_MAP.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue()
                        .reversed())
                .forEachOrdered(entry -> result.put(entry.getKey(), entry.getValue()));
        System.out.println("\nuser_count_map view:");
        System.out.println(JSON.toJSONString(result));
    }

    private static void printLatestIncrement() {
        Map<String, Integer> increment = new HashMap<>(MonthCount.NOV_COUNT_MAP);
        USER_COUNT_MAP.forEach((cnName, nowCount) -> {
            if (increment.containsKey(cnName)) {
                increment.computeIfPresent(cnName, (k, v) -> nowCount - v);
            } else {
                increment.put(cnName, nowCount);
            }
        });
        System.out.println("\nincrement_count view:");
        Map<String, Integer> result = Maps.newLinkedHashMapWithExpectedSize(increment.size());
        increment.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue()
                        .reversed())
                .forEachOrdered(entry -> result.put(entry.getKey(), entry.getValue()));
        System.out.println(JSON.toJSONString(result));
    }

    private static void printMonthUserCountMap() {
        Map<String, Map<String, Integer>> result = new LinkedHashMap<>();
        MONTH_USER_COUNT_MAP.forEach((month, userCountMap) -> {
            Map<String, Integer> map = Maps.newLinkedHashMapWithExpectedSize(userCountMap.size());
            userCountMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue()
                            .reversed())
                    .forEachOrdered(entry -> map.put(entry.getKey(), entry.getValue()));
            result.put(month, map);
        });
        System.out.println("\nmonth_user_count_map view:");
        System.out.println(JSON.toJSONString(result));
    }

    private static void search(File file) throws IOException {
        File[] fileArr;
        if (!file.exists() || Objects.isNull(fileArr = file.listFiles())) {
            return;
        }
        for (File subFile : fileArr) {
            if (subFile.isDirectory()) {
                search(subFile);
            } else {
                if (subFile.getName().endsWith(".java")) {
                    readFile(subFile);
                }
            }
        }
    }

    private static void readFile(File file) throws IOException {
        String fileName = file.getName();
        try (FileInputStream stream = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                match(fileName, line);
//                System.out.println(line);
            }
            stream.close();
            reader.close();
        }
    }

    private static void match(String fileName, String content) {
        Matcher matcher = PATTERN.matcher(content);
        while (matcher.find()) {
            String group = matcher.group();
            String anno = group.substring(0, group.indexOf("("));
            if (!ANNOTATIONS.contains(anno)) {
                continue;
            }

            if (!group.contains("developer")) {
                continue;
            }

            String sbuContent = group.replace("(", "").replace(")", "");
            String[] split = sbuContent.split(",");
            String developerInfo = null, unicode = null, developerName;
            if (split.length < 3) {
                return;
            }
            for (String str : split) {
                if (str.trim().startsWith("developer")) {
                    developerInfo = str;
                }
                if (str.trim().startsWith("uniqueCode")) {
                    unicode = str;
                }
            }
            if (StringUtils.isBlank(developerInfo)
                    || StringUtils.isBlank(unicode)
                    || StringUtils.isBlank(developerName = getDeveloperName(developerInfo))) {
                continue;
            }

//            UNI_CONTENT_LIST.remove(group);

            if (unicode.trim().startsWith("uniqueCode = \"" + PREFIX_12)) {
                if (Objects.equals(developerName, "LANGE")) {
                    CUR_MONTH_DETAIL.add(fileName + ": " + group);
                }
                statistic(developerName, "202312");
            }
            if (unicode.trim().startsWith("uniqueCode = \"" + PREFIX_11)) {
                statistic(developerName, "202311");
            }
            if (unicode.trim().startsWith("uniqueCode = \"" + PREFIX_10)) {
                statistic(developerName, "202310");
            }
            if (unicode.trim().startsWith("uniqueCode = \"" + PREFIX_09)) {
                statistic(developerName, "202309");
            }

        }
    }

    private static void statistic(String deName, String monthKey) {
        String cnKey = USER_MAP.getOrDefault(deName, deName);
        USER_COUNT_MAP.put(cnKey, USER_COUNT_MAP.getOrDefault(cnKey, 0) + 1);
        Map<String, Integer> map = MONTH_USER_COUNT_MAP.computeIfAbsent(monthKey, k -> new HashMap<>());
        map.put(cnKey, map.getOrDefault(cnKey, 0) + 1);
    }

    private static String getDeveloperName(String developerInfo) {
        if (!developerInfo.contains(".")) {
            return null;
        }
        developerInfo = developerInfo.replace(")", "");
        String[] developerArr = developerInfo.split("\\.");
        return developerArr[developerArr.length - 1];
    }

    private static void parseUser() throws IllegalAccessException {
        Class<User> clazz = User.class;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                USER_MAP.put(field.getName(), (String) field.get(clazz));
            }
        }
    }

}
