package com.zans.portal.vo.ip;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class IpSearchVO extends BasePage {

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "点位名称")
    private String point;

    @ApiModelProperty(value = "承建单位")
    private String contractor;

    @ApiModelProperty(value = "项目名称")
    private String project;

    @ApiModelProperty(value = "设备类型")
    @JSONField(name = "device_type")
    private Integer deviceType;

    @ApiModelProperty(value = "认证状态")
    @JSONField(name = "auth_status")
    private Integer authStatus;

    @ApiModelProperty(value = "所属辖区")
    private Integer area;

    @JSONField(name = "project_status")
    @ApiModelProperty(name = "project_status", value = "项目维护状态")
    private Integer projectStatus;

    @JSONField(name = "maintain_company")
    @ApiModelProperty(name = "maintain_company", value = "维护单位")
    private String maintainCompany;

    @JSONField(name = "vlan")
    @ApiModelProperty(name = "vlan", value = "vlan")
    private String vlan;

}
