package com.lingDream.utils;



import com.lingDream.utils.strUtil.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import static com.lingDream.utils.fileUtil.FileUtils.getClassLoaderInputStream;


public class MapUtils {
    /**
     * 获取两个Map集合的交集,
     * 返回新的Map集合
     *
     * @param map1        第一个map
     * @param map2        第二个map
     * @param retainWhich 想要保留哪一个map的值,输入2表示第二个,其它情况都默认保留第一个
     */
    public static Map mapSetIntersection(Map map1, Map map2, int retainWhich) {
        Set set1 = map1.keySet();
        Set set2 = map2.keySet();
        Map map = new HashMap();
        for (Object o1 : set1) {
            for (Object o2 : set2) {
                if (o1.equals(o2)) {
                    if (retainWhich == 2) map.put(o2, map2.get(o2));
                    else map.put(o1, map1.get(o1));
                    break;
                }
            }
        }
        return map;
    }

    /**
     * 根据java标准properties文件读取信息<br>
     * 并赋值为一个 HashMap<String,String>
     *
     * @param path properties地址
     * @return Properties 转换为Map
     */
    public static Map<String, String> fileToMap(String path) {
        if (!StringUtils.checkStr(".*.properties", path)) path += ".properties";
        Map<String, String> map = new HashMap<>();
        InputStream stream = getClassLoaderInputStream(path);
        Properties properties = new Properties();
        try {
            properties.load(stream);
            Set<Entry<Object, Object>> entries = properties.entrySet();
            for (Entry<Object, Object> entry : entries) {
                if (!entry.getKey().toString().startsWith("#")) {
                    map.put(((String) entry.getKey()).trim(), ((String) entry.getValue()).trim());
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}