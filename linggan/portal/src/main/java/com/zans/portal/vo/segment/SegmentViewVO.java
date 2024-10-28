package com.zans.portal.vo.segment;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author xv
 * @since 2020/6/9 19:32
 */
@Data
public class SegmentViewVO {

    private Integer id;

    private String name;

    @JSONField(name = "segment_short")
    private String segmentShort;

    private List<Map> segments;

    @JSONField(name = "ip_count")
    private Integer ipCount;

    @JSONField(name = "sw_ip")
    private String swIp;

    private Integer tag;

    private Integer enable;

    @JSONField(name = "enableName")
    private String enableName;

    public void setEnableNameByMap(Map<Object, String> map) {
        Integer status = this.enable;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setEnableName(name);
    }
}
