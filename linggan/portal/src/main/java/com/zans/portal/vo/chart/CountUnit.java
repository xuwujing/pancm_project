package com.zans.portal.vo.chart;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * GroupUnit
 * @author xv
 * @since 2020-03-04
 */
@Data
@Slf4j
public class CountUnit {

    private String id;

    @JSONField(name = "name")
    private String countName;

    @JSONField(serialize = false)
    private Object countKey;

    @JSONField(name = "value")
    private Object val;

    public void resetCountName(Map<Object, String> map) {
        if (map == null || this.countKey == null) {
            return;
        }
        String name = map.get(this.countKey);
        if (name == null) {
            return;
        }
        this.setCountName(name);
    }
}
