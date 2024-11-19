package com.pancm.test.design.log.strategy;

import cn.hutool.core.util.DesensitizedUtil;

import java.util.regex.Pattern;

public class PhoneSensitiveStrategy implements SensitiveStrategy {
    @Override
    public Pattern pattern() {
        return Pattern.compile("\\b1[3-9]\\d{9}\\b");
    }

    @Override
    public String resolve(String message) {
        return DesensitizedUtil.mobilePhone(message);
    }
}
