package com.lingDream.utils;

/**
 * @Author: LI_Lingfei
 * @CreateTime: 2020-07-25 15:35
 */
public class ArrayUtils {
    /**
     * 返回一个值在数组中的位置(第一次出现)
     * 找到返回它的索引,
     * 没找到返回-1
     *
     * @param array 数组对象
     * @param val   需要查找的值
     * @return 值出现的位置
     */
    public static int getArrayIndex(Object[] array, Object val) {
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(val)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
