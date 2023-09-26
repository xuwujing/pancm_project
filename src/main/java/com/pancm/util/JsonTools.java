package com.pancm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Title: JsonTools
 * Description: fastJson处理工具包
 * Version:1.0.0
 *
 * @author panchengming
 * @date 2017年9月10日
 */
public class JsonTools {

    /**
     * JSON 转换为 JavaBean
     *
     * @param <T>  the type parameter
     * @param json the json
     * @param t    the t
     * @return <T>
     */
    public static <T> T toBean(JSONObject json,Class<T> t){
		 return JSON.toJavaObject(json,t);  
	}

    /**
     * JSON 字符串转换为 JavaBean
     *
     * @param <T> the type parameter
     * @param str the str
     * @param t   the t
     * @return <T>
     */
    public static <T> T toBean(String str,Class<T> t){
		 return JSON.parseObject(str,  t);  
	}

    /**
     * JavaBean 转化为JSON
     *
     * @param t the t
     * @return json object
     */
    public static  JSONObject toJson(Object t){
		return (JSONObject) JSON.toJSON(t);
	}

    /**
     * JSON 字符串转换为 HashMap
     *
     * @param json - String
     * @return Map map
     */
    @SuppressWarnings("rawtypes")
	public static Map toMap(String json) {
		if (json == null || "".equals(json.trim())) {
			return new HashMap();
		}
		return JSON.parseObject(json, HashMap.class);
	}

    /**
     * 将map转化为string
     *
     * @param m the m
     * @return string
     */
    @SuppressWarnings("rawtypes")
	public static String toString(Map m) {  
        return JSONObject.toJSONString(m);  
    }

    /**
     * 转换为数组
     *
     * @param <T>  the type parameter
     * @param text the text
     * @return object [ ]
     */
    public static <T> Object[] toArray(String text) {
        return toArray(text, null);  
    }

    /**
     * 转换为数组
     *
     * @param <T>   the type parameter
     * @param text  the text
     * @param clazz the clazz
     * @return object [ ]
     */
    public static <T> Object[] toArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz).toArray();  
    }  
    
}
