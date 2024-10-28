package com.zans.portal.vo.asset.resp;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel
@Data
public class AssetScanRespVO extends BasePage {

    @ApiModelProperty(value = "id")
    @JSONField(name = "id")
    private Integer id;

    @ApiModelProperty(value = "名称")
    @JSONField(name = "name")
    private String name;

    @ApiModelProperty(value = "IP")
    @JSONField(name = "ip_addr")
    private String ipAddr;

    @ApiModelProperty(value = "型号")
    @JSONField(name = "model")
    private String model;

    @ApiModelProperty(value = "首次上线")
    @JSONField(name = "begin_time")
    private String beginTime;

    @ApiModelProperty(value = "在线时长")
    @JSONField(name = "online_time")
    private String onlineTime;

    @ApiModelProperty(value = "离线时长")
    @JSONField(name = "offline_time")
    private String offlineTime;

    @ApiModelProperty(name = "offlineType",value = "在线状态 离线原因：0.断光,1掉电")
    private Integer offlineType;

    @ApiModelProperty(name = "alive", value = "在线状态 1:在线；2；不在线")
    private Integer alive;
}
