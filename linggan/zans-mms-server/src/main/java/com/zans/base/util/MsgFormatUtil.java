package com.zans.base.util;

import com.alibaba.fastjson.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pancm
 * @Title: zans-mms-server
 * @Description: 消息格式化工具类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/3/11
 */
public class MsgFormatUtil {

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

    /**
     * 格式化字符串 字符串中使用{key}表示占位符
     *
     * @param sourStr 需要匹配的字符串
     * @param param   参数集
     * @return
     */
    public  static String format(String sourStr, JSONObject param) {
        Matcher matcher = PATTERN.matcher(sourStr);
        while (matcher.find()) {
            String key = matcher.group();
            String keyClone = key.substring(1, key.length() - 1).trim();
            Object value = param.get(keyClone);
            if (value != null)
                sourStr = sourStr.replace(key, value.toString());
        }
        return sourStr;
    }


}
