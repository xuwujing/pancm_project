package com.zans.util;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Method;

/**
 * @author beixing
 * @Title: consumer
 * @Description: 反射工具类
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/11/24
 */
public class ReflectionUtil {

    /**
    * @Author beixing
    * @Description
    * @Date  2021/11/24
    * @Param
    * @return
    **/
    public static Object getMethodValue(String className, String methodName, JSONObject args) throws ReflectiveOperationException {
        //指定类和路径
        Class clazz=Class.forName(className);
        //获取私有的方法，必须要使用getDeclaredMethod
        //获取公有的方法
        Method method=clazz.getMethod(methodName,JSONObject.class);
        Object obj =method.invoke(clazz.newInstance(), args);
        return obj;
    }

}
