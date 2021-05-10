package com.lingDream.root.utils.strUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;


/**
 * @Author: LI_Lingfei
 * @CreateTime: 2020-07-18 18:15
 * 字符串相关工具
 */
public class StringUtils {
    /**
     * 将字符串的首字母转大写
     *
     * @param str 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String uppFirstChar(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 将字符串的首字母转小写
     *
     * @param str 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String lowFirstChar(String str) {
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * @param regex 正则表达式规则
     * @param str   需要检查的字符串
     * @return 是否符合正则表达式的规则
     */
    public static boolean checkStr(String regex, String str) {
        return compile(regex).matcher(str).matches();
    }

    /**
     * 传入正则表达式,与需要操作的字符串,
     * 返回该字符串中符合
     * 正则表达式条件的所有字符或字符串List集合
     */
    public static List<String> truncatedString(String regex, String str) {
        Matcher matcher = compile(regex).matcher(str);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    /**
     * 用于将中文字符串转为拼音
     * 如果传入的原本就是英文或者数字,
     * 则保持不变
     *
     * @param chinese 传入的文字
     * @return 拼写好的拼音字符串
     */
    public static String toPinyin(String chinese) {
        PinyinUtil py = new PinyinUtil();
        return py.getStringPinYin(chinese);
    }

    /**
     * 下划线命名转驼峰命名
     *
     * @param underscore 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String underscoreToCamelCase(String underscore) {
        String[] ss = underscore.split("_");
        if (ss.length == 1) {
            return underscore;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(ss[0]);
        for (int i = 1; i < ss.length; i++) {
            sb.append(uppFirstChar(ss[i]));
        }

        return sb.toString();
    }

    /**
     * 驼峰 转下划线
     *
     * @param camelCase
     * @return
     */
    public static String toLine(String camelCase) {
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(camelCase);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String get_set(String s, String set) {
        String s1 = uppFirstChar(s);
        return set + s1;
    }
}
