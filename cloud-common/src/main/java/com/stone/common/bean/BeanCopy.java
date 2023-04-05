package com.stone.common.bean;

import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BeanCopy {

    private static final ConcurrentMap<ClassLoader, ConcurrentMap<String, ConcurrentMap<String, BeanCopier>>> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();

    public static <S, T> List<T> copyList(List<S> sourceList, Class<T> targetClass) {
        if (Objects.isNull(sourceList)) {
            return new ArrayList<>();
        }

        List<T> targetList = new ArrayList<>(sourceList.size());
        for (S source : sourceList) {
            targetList.add(copy(source, targetClass));
        }
        return targetList;
    }

    public static <S, T> T copy(S sourceObject, Class<T> targetClass) {
        if (sourceObject == null) {
            return null;
        }

        BeanCopier copier = BEAN_COPIER_CACHE.computeIfAbsent(sourceObject.getClass().getClassLoader(), k -> new ConcurrentHashMap<>())
                .computeIfAbsent(sourceObject.getClass().getCanonicalName(), k -> new ConcurrentHashMap<>((int) Math.ceil(4 / 0.75)))
                .computeIfAbsent(targetClass.getCanonicalName(), k -> BeanCopier.create(sourceObject.getClass(), targetClass, false));

        T targetObject;
        try {
            targetObject = targetClass.newInstance();
            copier.copy(sourceObject, targetObject, null);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return targetObject;
    }


}
