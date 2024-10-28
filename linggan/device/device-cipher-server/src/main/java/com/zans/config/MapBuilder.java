package com.zans.config;

import com.alibaba.fastjson.JSON;
import com.zans.constant.EnumErrorCode;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapBuilder {

    private Map<String,Object> map;

    private MapBuilder() {
        this.map = new LinkedHashMap<>();
    }

    public static MapBuilder getBuilder() {
        return new MapBuilder();
    }

    public static Map<String,Object> getSimpleMap(String key, Object value) {
        return getBuilder().put(key, value).build();
    }

    public static Map<String,Object> getSimpleMap(EnumErrorCode code) {
        return getBuilder().put(code).build();
    }

    public MapBuilder put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public MapBuilder put(EnumErrorCode code) {
        this.map.put("code", code.getCode());
        this.map.put("message", code.getMessage());
        return this;
    }

    public MapBuilder append(String key, Object appendValue) {
        if (appendValue == null ) {
            return this;
        }
        Object oldValue = this.map.get(key);
        if (oldValue == null) {
            this.map.put(key, appendValue);
        } else {
            this.map.put(key,oldValue.toString() + ", " + appendValue.toString());
        }
        return this;
    }

    public Object get(String key) {
        return this.map.get(key);
    }

    public Map<String, Object> build() {
        return this.map;
    }

    public String toJsonString() {
        return JSON.toJSONString(this.map);
    }

    public MapBuilder remove(String key) {
        this.map.remove(key);
        return this;
    }

    public MapBuilder replace(String key, Object value) {
        this.map.replace(key, value);
        return this;
    }
}
