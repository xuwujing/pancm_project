package com.pancm.test.design.log.strategy;

import java.util.regex.Pattern;

public interface SensitiveStrategy {

    /**
     * 匹配正则
     * @return
     */
    Pattern pattern();

    /**
     * 处理匹配之后结果
     * @param message 匹配出来的字符
     * @return
     */
    String resolve(String message);


}
