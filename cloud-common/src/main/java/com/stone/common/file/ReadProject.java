package com.stone.common.file;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.util.*;

public class ReadProject {

    private static final Map<String, Integer> COUNT_MAP = new HashMap<>();

    private static final Map<String, Map<String, Integer>> COUNT_BY_FILE_MAP = new HashMap<>();

    public static void main(String[] args) throws IOException {
//        String path = "/Users/stone/IdeaProjects/yt/slt";
        String path = "/Users/stone/code/yt/slt";
        search(new File(path));
        printCountMap();
        printFileCountMap();
        printFileCount(COUNT_BY_FILE_MAP.get("LANGE"));
    }

    private static void printCountMap() {
        System.out.println("\n\n--> 数量总览");
        System.out.println(JSON.toJSONString(COUNT_MAP));
    }

    private static void printFileCountMap() {
        System.out.println("\n\n--> 文件数量总览");
        System.out.println(JSON.toJSONString(COUNT_BY_FILE_MAP));
    }

    private static void printFileCount(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
        entries.sort((a, b) -> b.getValue() - a.getValue());
        System.out.println("\n\n--> 数量排名");
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
            String str;
            while ((str = reader.readLine()) != null) {
                match(fileName, str);
//                System.out.println(str);
            }
            //close
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
                    String[] strArr = s.split("\\.");
                    addCount(fileName, strArr[strArr.length - 1]);
                }
            }
        }
    }

    private static void addCount(String fileName, String key) {
        Integer count = COUNT_MAP.getOrDefault(key, 0);
        COUNT_MAP.put(key, count + 1);
        Map<String, Integer> fileNameMap = COUNT_BY_FILE_MAP.computeIfAbsent(key, k -> new HashMap<>());
        Integer countByFileName = fileNameMap.getOrDefault(fileName, 0);
        fileNameMap.put(fileName, countByFileName + 1);
    }
}
