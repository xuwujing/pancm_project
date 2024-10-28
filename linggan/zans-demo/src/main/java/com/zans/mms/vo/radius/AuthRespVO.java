package com.zans.mms.vo.radius;


import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@ApiModel
@Data
public class AuthRespVO {

    private Integer id;

    @ApiModelProperty(name = "mac", value = "MAC地址")
    private String mac;

    @ApiModelProperty(name = "reply_status", value = "认证结果")
    @JSONField(name = "reply_status")
    private Integer replyStatus;

    @ApiModelProperty(name = "reply_status_name", value = "认证结果名称")
    @JSONField(name = "reply_status_name")
    private String replyStatusName;

    @ApiModelProperty(name = "auth_date", value = "认证日期")
    @JSONField(name = "auth_date", format = "yyyy-MM-dd HH:mm:ss")
    private Date authDate;

    @ApiModelProperty(name = "company", value = "设备厂商")
    private String company;

    @ApiModelProperty(name = "nas_type", value = "接入类型")
    @JSONField(name = "nas_type")
    private String nasType;

    @ApiModelProperty(name = "nas_ip", value = "接入IP")
    @JSONField(name = "nas_ip")
    private String nasIp;

    @ApiModelProperty(name = "nas_port", value = "接入端口")
    @JSONField(name = "nas_port")
    private String nasPort;

    @ApiModelProperty(name = "nas_area", value = "接入区域")
    @JSONField(name = "nas_area")
    private Integer nasArea;

    @ApiModelProperty(name = "nas_area_name", value = "接入区域名称")
    @JSONField(name = "nas_area_name")
    private String nasAreaName;

    @ApiModelProperty(name = "aaa_mac", value = "3A认证的mac")
    @JSONField(name = "aaa_mac")
    private String aaaMac;

    public void setReplyStatusByMap(Map<Object, String> map) {
        Integer status = this.getReplyStatus();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setReplyStatusName(name);
    }
}
