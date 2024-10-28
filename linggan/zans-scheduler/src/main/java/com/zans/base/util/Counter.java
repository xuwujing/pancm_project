package com.zans.base.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xv
 * @since 2020/5/13 13:31
 */
public class Counter {

    private Map<Object, Integer> map = new HashMap<>(10);

    public void init(Object key, Integer val) {
        this.map.put(key, val);
    }

    public void increase(Object key) {
        increase(key, 1);
    }

    public void decrease(Object key) {
        increase(key, -1);
    }

    public void decrease(Object key, int step) {
        increase(key, - step);
    }

    public void increase(Object key, int step) {
        Integer val = map.get(key);
        if (val == null) {
            val = 0;
        }
        map.put(key, val + step);
    }

    public Integer get(Object key) {
        return map.get(key);
    }

    public boolean lqZero(Object key) {
        Integer val = map.get(key);
        if (val == null) {
            return true;
        }
        return val <= 0;
    }
}
