package com.zans.portal.vo.switcher;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel
@Data
public class SwitcherInterfaceRespVO extends BasePage {

    @ApiModelProperty(value = "id")
    @JSONField(name = "id")
    private Integer id;

    @ApiModelProperty(value = "sw_id")
    @JSONField(name = "sw_id")
    private Integer swId;

    @ApiModelProperty(value = "mac地址")
    @JSONField(name = "mac")
    private String mac;

    @ApiModelProperty(value = "mac接口序号")
    @JSONField(name = "interface_index")
    private Integer interfaceIndex;

    @ApiModelProperty(value = "mac接口名称")
    @JSONField(name = "interface_detail")
    private String interfaceDetail;

    @ApiModelProperty(value = "mac接口序号")
    @JSONField(name = "mac_index")
    private Integer macIndex;

    @ApiModelProperty(value = "启用状态")
    @JSONField(name = "up_status")
    private Integer upStatus;

    @ApiModelProperty(value = "上行/下行")
    @JSONField(name = "stream_type")
    private Integer streamType;

    @ApiModelProperty(value = "下行设备总数")
    @JSONField(name = "scan_mac_all")
    private Integer scanMacAll;

    @ApiModelProperty(value = "活跃下行设备")
    @JSONField(name = "scan_mac_alive")
    private Integer scanMacAlive;

}
