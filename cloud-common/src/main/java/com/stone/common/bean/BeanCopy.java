package com.stone.common.bean;

import net.sf.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BeanCopy {
    private static ConcurrentMap<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<>();

    public static <T> List<T> doConvertListDto(List list, Class<T> dtoClz) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<T> dtoList = new ArrayList<>();
        for (Object o : list) {
            dtoList.add(doConvertDto(o, dtoClz));
        }
        return dtoList;
    }

    public static <T> T doConvertDto(Object doObj, Class<T> dtoClz) {
        if (doObj == null) {
            return null;
        }

        String beanKey = generateKey(doObj.getClass(), dtoClz);
        BeanCopier copier;

        if (!beanCopierMap.containsKey(beanKey)) {
            // 多线程环境下copier可能会被创建多次，但未被存到ConcurrentMap的copier会在该方法结束后被销毁，不会造成影响，此方法被调用频次高，所以未使用重量级的加锁机制
            copier = BeanCopier.create(doObj.getClass(), dtoClz, false);
            beanCopierMap.putIfAbsent(beanKey, copier);
        } else {
            copier = beanCopierMap.get(beanKey);
        }

        T target;
        try {
            target = dtoClz.newInstance();
            copier.copy(doObj, target, null);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return target;
    }

    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.getCanonicalName() + "_to_" + class2.getCanonicalName();
    }

    public static <T> T dtoConvertDo(Object dtoObj, Class<T> doClz) {
        return doConvertDto(dtoObj, doClz);
    }

}
