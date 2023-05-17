package com.pancm.test.utilTest;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据转换
 *
 * @author
 */
public class StructConvertUtil {

    public static <S, T> T convert(S sourceObj, Class<T> targetClass) throws Exception {
        if (null == sourceObj) {
            throw new RuntimeException("[StructConvertor.convert]:sourceObj is null");
        }
        if (null == targetClass) {
            throw new RuntimeException("[StructConvertor.convert]:targetClass is null");
        }
        T targetObj = targetClass.newInstance();
        BeanUtils.copyProperties(sourceObj, targetObj);
        return targetObj;

    }

    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass) throws Exception {
        if (sourceList == null || sourceList.size() <= 0) {
            return new ArrayList<T>(0);
        }
        List<T> targetList = new ArrayList<T>();
        for (S e : sourceList) {
            T t = convert(e, targetClass);
            targetList.add(t);
        }
        return targetList;
    }

    public static <K, S, T> Map<K, T> convertMap(Map<K, S> sourceMap, Class<T> targetClass) throws Exception {
        if (sourceMap == null || sourceMap.size() <= 0) {
            return new HashMap<K, T>(0);
        }
        Map<K, T> targetMap = new HashMap<K, T>();
        for (Map.Entry<K, S> entry : sourceMap.entrySet()) {
            S value = entry.getValue();
            T t = convert(value, targetClass);
            targetMap.put(entry.getKey(), t);
        }
        return targetMap;
    }
}