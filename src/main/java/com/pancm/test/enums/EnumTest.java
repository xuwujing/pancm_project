package com.pancm.test.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/8/24
 */
public class EnumTest {
    public static void main(String[] args) {

        System.out.println(ConstractMongoStatus.getDesc(1));
        //枚举转map
        Map<Integer, String> stringMap = Arrays.stream(ConstractMongoStatus.values())
                .collect(Collectors.toMap(ConstractMongoStatus::getStatus, ConstractMongoStatus::getDesc));
        System.out.println(stringMap);
    }
}
