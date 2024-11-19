package com.pancm.test.design.log.strategy;

import cn.hutool.core.util.DesensitizedUtil;

import java.util.regex.Pattern;

public class EmailSensitiveStrategy implements SensitiveStrategy{
    @Override
    public Pattern pattern() {
        //匹配email的正则表达式
        return Pattern.compile("\\b([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})\\b");
    }

    @Override
    public String resolve(String message) {
        return DesensitizedUtil.email(message);
    }
}

