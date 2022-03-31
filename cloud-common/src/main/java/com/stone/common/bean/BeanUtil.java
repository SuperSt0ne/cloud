package com.stone.common.bean;

import net.sf.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BeanUtil {

    /**
     * 将对象转换为map
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key.toString(), beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将map转换为javabean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) throws Exception {
        T bean = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            if(field.getType().equals(Date.class)){
                Object val = map.get(field.getName());
                if(val instanceof Long){
                    map.put(field.getName(), new Date((Long)val));
                }
            }
            if(field.getType().equals(Long.class)){
                Object val = map.get(field.getName());
                if(val instanceof Integer){
                    map.put(field.getName(), ((Integer)val).longValue());
                }
            }
        }
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }
}
