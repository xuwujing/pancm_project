package com.zans.portal.vo.segment;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author xv
 * @since 2020/6/9 19:58
 */
@Data
public class SegmentEditVO {

    @NotNull(message = "ID必填")
    @ApiModelProperty(value = "ID，主键")
    private Integer id;

    @NotEmpty(message = "名称必填")
    private String name;

    @JSONField(name = "segment_short")
    private String segmentShort;

    @NotEmpty(message = "IP地址段必填")
    private String[] segment;


    @JSONField(name = "sw_ip")
    private String swIp;

    private Integer tag;

    private Integer enable;

    @JSONField(name = "ip_count")
    private Integer ipCount;

}
