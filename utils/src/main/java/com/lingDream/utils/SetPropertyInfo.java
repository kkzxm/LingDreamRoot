package com.lingDream.utils;

import com.lingDream.utils.strUtil.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: LI_Lingfei
 * @CreateTime: 2021-01-08 20:11
 */
public class SetPropertyInfo {
    public static Object setPropertyInfo(Object object, String str, String val) {
        String[] split = str.split("[.]");
        List<String> list = new ArrayList<>();
        for (String s : split) {
            list.add(s);
        }
        return superSetPropertyVal(object, list, val);
    }

    /**
     * 超级属性赋值器
     *
     * @param object
     * @param list
     * @param val
     */
    public static Object superSetPropertyVal(Object object, List<String> list, String val) {
        Class<?> aClass = object.getClass();
        Class<?> type = null;
        String s = list.get(0);
        String set = StringUtils.get_set(list.get(0), "set");
        String get = StringUtils.get_set(list.get(0), "get");

        try {
            Field declaredField = aClass.getDeclaredField(s);
            type = declaredField.getType();
            Object objVal = aClass.getMethod(get).invoke(object);
            if (objVal == null) objVal = newObject(type);
            if (list.size() == 1) objVal = val;
            aClass.getMethod(set, type).invoke(object, objVal);
            if (list.size() > 1) {
                list.remove(0);
                superSetPropertyVal(objVal, list, val);
            }
            return object;
        } catch (Exception ignored) {
            try {
                if (type != null && Number.class.isAssignableFrom(type)) {
                    Object o = type.getConstructor(String.class).newInstance(val);
                    aClass.getMethod(set, type).invoke(object, o);
                }
            } catch (Exception ignored1) {
            }
        }
        return object;
    }

    public static <T> T setObInfo(Map<String, String> map, T object) {
        Set<String> strings = map.keySet();
        for (String string : strings) {
            setPropertyInfo(object, string, map.get(string));
        }
        return object;
    }

    private static Object newObject(Class<?> type) throws IllegalAccessException, InstantiationException {
        return type.newInstance();
    }
}

