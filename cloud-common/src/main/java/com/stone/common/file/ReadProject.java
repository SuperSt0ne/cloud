package com.stone.common.file;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReadProject {

    private static final Map<String, Integer> COUNT_MAP = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String path = "/Users/stone/IdeaProjects/yt/slt";
        search(new File(path));
        System.out.println(COUNT_MAP);
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
                    System.out.println(subFile.getAbsolutePath());
                    readFile(subFile);
                }
            }
        }
    }

    private static void readFile(File file) throws IOException {
        try (FileInputStream stream = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String str;
            while ((str = reader.readLine()) != null) {
                match(str);
//                System.out.println(str);
            }
            //close
            stream.close();
            reader.close();
        }
    }

    private static void match(String content) {
        if ((content = content.trim()).startsWith("@Biz") && !content.startsWith("@BizSqlMethod")) {
            for (String s : content.split(",")) {
                if ((s = s.trim()).startsWith("developer")) {
                    if (!s.contains(".")) {
                        return;
                    }
                    String[] strArr = s.split("\\.");
                    addCount(strArr[strArr.length - 1]);
                }
            }
        }
    }

    private static void addCount(String key) {
        Integer count = COUNT_MAP.getOrDefault(key, 0);
        COUNT_MAP.put(key, count + 1);
    }
}
