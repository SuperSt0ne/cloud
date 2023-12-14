package com.stone.common.util;

public class ObjTool {

    public static <T> T get(T val, T defaultVal) {
        return val != null ? val : defaultVal;
    }

}
