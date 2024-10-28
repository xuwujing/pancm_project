package com.zans.base.office.annotation;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * ExcelProperty 的匹配函数，用于命中 header
 * @author xv
 * @since 2020/3/27 14:08
 */
@Slf4j
public class ExcelPropertyMatcher {

    /**
     * 字符串匹配
     * @param input 输入字符串，abcd
     * @param pattern 有4种， %abcd%， %cd, ab%， abcd
     * @return 是否命中匹配模式
     */
    public static boolean match(String input, String pattern) {
        if (input == null || pattern == null) {
            return false;
        }
        boolean start = pattern.startsWith("%");
        boolean end = pattern.endsWith("%");
        String target = pattern;
        if (start) {
            target = target.substring(1);
        }
        if (end) {
            target = target.substring(0, target.length() - 1);
        }
//        log.info("target#{}, {}, start#{}, end#{}", target, pattern, start, end);
        if (start && end) {
            return input.contains(target);
        } else if(start) {
            return input.endsWith(target);
        } else if (end) {
            return input.startsWith(target);
        } else {
            return input.equals(target);
        }
    }

    /**
     * 匹配一组输入
     * @param keys 待命中的集合
     * @param pattern 匹配模式
     * @return 命中的key
     */
    public static String getMatchKey(Set<String> keys, String pattern) {
        for (String key : keys) {
            if (match(key, pattern)) {
                return key;
            }
        }
        return null;
    }
}
