package com.zans.portal.vo.segment;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

/**
 * @author xv
 * @since 2020/6/9 19:58
 */
@Data
public class SegmentAddVO {

    //ip地址名称
    @NotEmpty(message = "名称必填")
    private String name;

    @NotEmpty(message = "IP地址段必填")
    private String[] segment;

    @JSONField(name = "segment_short")
    private String segmentShort;

    //交换机ip
    @JSONField(name = "sw_ip")
    private String swIp;

    //交换机标签
    private Integer tag;

    //是否启动
    private Integer enable;

    @JSONField(name = "ip_count")
    private Integer ipCount;

}
