package com.stone.common.exception;

import java.util.Objects;

public class ExceptionUtil {

    public static String getStackTrace(Throwable throwable, int maxDepth) {
        Objects.requireNonNull(throwable);
        if (maxDepth <= 0) {
            throw new IllegalArgumentException("maxDepth cannot be less than or equal to 0");
        }

        StackTraceElement[] stackTraceElements = throwable.getStackTrace();

        StringBuilder stackTraceBuilder = new StringBuilder();
        stackTraceBuilder.append("class: ").append(throwable.getClass().getName()).append(System.lineSeparator());
        stackTraceBuilder.append("message: ").append(throwable.getMessage()).append(System.lineSeparator());
        for (int i = 0; i < stackTraceElements.length && i < maxDepth; i++) {
            stackTraceBuilder.append(stackTraceElements[i]).append(System.lineSeparator());
        }
        return stackTraceBuilder.toString();
    }

}
