package com.stone.common.file;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ReadProject {

    private static final Map<String, String> USER_MAP = new HashMap<>();

    private static final Map<String, Integer> COUNT_MAP = new HashMap<>();

    private static final Map<String, Map<String, Integer>> COUNT_BY_FILE_MAP = new HashMap<>();

    private static final String PATH_MAC = "/Users/stone/IdeaProjects/yt/slt";

    private static final String PATH_MAC_MINI = "/Users/stone/code/yt/slt";

    static {
        try {
            parseUser();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        search(new File(PATH_MAC));
        printCountMap();
        printNovIncrement();
        printFileCountMap();
    }

    private static void printNovIncrement() {
        Map<String, Integer> increment = new HashMap<>(MonthCount.OCT_COUNT_MAP);
        COUNT_MAP.forEach((cnName, nowCount) -> {
            if (increment.containsKey(cnName)) {
                increment.computeIfPresent(cnName, (k, v) -> nowCount - v);
            } else {
                increment.put(cnName, nowCount);
            }
        });
        System.out.println("\n\n--> nov increment count view:");
        Map<String, Integer> result = Maps.newLinkedHashMapWithExpectedSize(increment.size());
        increment.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue()
                        .reversed())
                .forEachOrdered(entry -> result.put(entry.getKey(), entry.getValue()));
        System.out.println(JSON.toJSONString(result));
    }

    private static void printCountMap() {
        System.out.println("\n\n--> count view:");
        System.out.println(JSON.toJSONString(COUNT_MAP));
    }

    private static void printFileCountMap() {
        System.out.println("\n\n--> count by file view:");
        System.out.println(JSON.toJSONString(COUNT_BY_FILE_MAP));

        printFileCount(COUNT_BY_FILE_MAP.get("兰戈"));
    }

    private static void printFileCount(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
        entries.sort((a, b) -> b.getValue() - a.getValue());
        System.out.println("\n\n--> count sort:");
        System.out.println(JSON.toJSONString(entries));
    }

    private static void search(File file) throws IOException {
        File[] fileArr = file.listFiles();
        if (Objects.isNull(fileArr)) {
            return;
        }
        for (File subFile : fileArr) {
            if (subFile.isDirectory()) {
                search(subFile);
            } else {
                if (subFile.getName().endsWith(".java")) {
//                    System.out.println(subFile.getAbsolutePath());
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
        if ((content = content.trim()).startsWith("@Biz") && !content.startsWith("@BizSqlMethod")) {
            for (String s : content.split(",")) {
                if ((s = s.trim()).startsWith("developer")) {
                    if (!s.contains(".")) {
                        return;
                    }
                    s = s.replace(")", "");
                    String[] strArr = s.split("\\.");
                    addCount(fileName, strArr[strArr.length - 1]);
                }
            }
        }
    }

    private static void addCount(String fileName, String key) {
        String cnKey = USER_MAP.getOrDefault(key, key);
        COUNT_MAP.put(cnKey, COUNT_MAP.getOrDefault(cnKey, 0) + 1);

        Map<String, Integer> fileNameMap = COUNT_BY_FILE_MAP.computeIfAbsent(cnKey, k -> new HashMap<>());
        Integer countByFileName = fileNameMap.getOrDefault(fileName, 0);
        fileNameMap.put(fileName, countByFileName + 1);
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
