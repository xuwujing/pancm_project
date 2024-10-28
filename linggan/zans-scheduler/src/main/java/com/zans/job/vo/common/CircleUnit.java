package com.zans.job.vo.common;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Map;

/**
 *      "type": 1,
 *       "name": "已认证",
 *       "value": 85
 *
 * @author xv
 * @since 2020/3/4 20:08
 */
@Data
public class CircleUnit {

    private Integer type;

    private String name;

    @JSONField(name = "value")
    private Object val;

    public void resetName(Map<Object, String> map) {
        if (map == null || this.type == null) {
            return;
        }
        String name = map.get(this.type);
        if (name == null) {
            return;
        }
        this.setName(name);
    }
}
