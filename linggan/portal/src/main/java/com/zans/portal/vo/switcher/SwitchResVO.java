package com.zans.portal.vo.switcher;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class SwitchResVO {

    private Integer id;

    @ApiModelProperty(name = "sw_type", value = "交换机类型")
    @JSONField(name = "sw_type")
    private Integer swType;

    @ApiModelProperty(name = "sys_desc", value = "交换机类型")
    @JSONField(name = "sys_desc")
    private String sysDesc;

    @ApiModelProperty(name = "sw_type_name", value = "交换机类型名称")
    @JSONField(name = "sw_type_name")
    private String swTypeName;

    @ApiModelProperty(name = "brand", value = "品牌")
    @JSONField(name = "brand")
    private Integer brand;

    @ApiModelProperty(name = "brand_name", value = "品牌")
    @JSONField(name = "brand_name")
    private String brandName;

    @ApiModelProperty(name = "sw_host", value = "ip")
    @JSONField(name = "sw_host")
    private String swHost;

    @ApiModelProperty(name = "radius_config", value = "redius认证")
    @JSONField(name = "radius_config")
    private Integer radiusConfig;

    @ApiModelProperty(name = "radius_config_Name", value = "redius认证值")
    @JSONField(name = "radius_config_Name")
    private String radiusConfigName;

    @ApiModelProperty(name = "remark", value = "点位名称")
    @JSONField(name = "remark")
    private String remark;

    @JSONField(name = "scan_interface_count")
    private Integer scanInterfaceCount;

    @JSONField(name = "scan_mac_alive")
    private Integer scanMacAlive;

    @JSONField(name = "scan_mac_all")
    private Integer scanMacAll;

    @ApiModelProperty(name = "version", value = "软件版本")
    @JSONField(name = "version")
    private String version;

    private String typeName;

}
