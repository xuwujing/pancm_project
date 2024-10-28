package com.zans.portal.vo.switcher;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel
@Data
public class SwitcherMacRespVO extends BasePage {

    @ApiModelProperty(value = "id")
    @JSONField(name = "id")
    private Integer id;

    @ApiModelProperty(value = "mac地址")
    @JSONField(name = "mac")
    private String mac;

    @ApiModelProperty(value = "mac接口序号")
    @JSONField(name = "interface_index")
    private Integer interfaceIndex;

    @ApiModelProperty(value = "mac接口名称")
    @JSONField(name = "interface_detail")
    private String interfaceDetail;

    @ApiModelProperty(value = "ip地址")
    @JSONField(name = "ip_addr")
    private String ipAddr;

    @ApiModelProperty(value = "在线状态")
    @JSONField(name = "alive")
    private Integer alive;

    @ApiModelProperty(value = "company")
    @JSONField(name = "company")
    private String company;

    @ApiModelProperty(value = "arp_id")
    @JSONField(name = "arp_id")
    private Integer arpId;

    @ApiModelProperty(value = "device_type_name")
    @JSONField(name = "device_type_name")
    private String deviceTypeName;

    @ApiModelProperty(value = "model_des")
    @JSONField(name = "model_des")
    private String modelDes;

    @ApiModelProperty(value = "point_name")
    @JSONField(name = "point_name")
    private String pointName;


}
