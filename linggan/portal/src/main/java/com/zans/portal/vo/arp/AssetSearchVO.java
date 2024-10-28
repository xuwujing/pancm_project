package com.zans.portal.vo.arp;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.MacPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class AssetSearchVO extends MacPage {

    @ApiModelProperty(value = "IP地址")
    private String ipAddr;

    @ApiModelProperty(value = "所属辖区")
    private Integer area;

    @ApiModelProperty(name = "device_type", value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceTypeDetect", value = "识别类型")
    private Integer deviceTypeDetect;


    @ApiModelProperty(value = "在线情况")
    private Integer alive;

    @ApiModelProperty(value = "是否哑终端")
    private Integer mute;

    @ApiModelProperty(value = "地址分配")
    private Integer disStatus;

    private Integer detectType;

    @ApiModelProperty(value = "厂商")
    private String company;

    @ApiModelProperty(value = "项目名称")
    private String project;

    @ApiModelProperty(value = "点位名称")
    private String point;

//    @JSONField(name = "project_status")
    @ApiModelProperty(name = "project_status", value = "项目维护状态")
    private Integer projectStatus;

//    @JSONField(name = "maintain")
    @ApiModelProperty(name = "maintain", value = "维护单位")
    private String maintain;

    @JSONField(name = "contractor")
    @ApiModelProperty(name = "contractor", value = "承建单位")
    private String contractor;

    @ApiModelProperty(name = "device_model", value = "设备型号")
//    @JSONField(name = "device_model")
    private String deviceModel;

    @ApiModelProperty(name = "alive_date", value = "离线")
//    @JSONField(name = "alive_date")
    protected List<String> aliveDateRange;

//    @JSONField(name = "alive_start_date")
    protected String aliveStartDate;

//    @JSONField(name = "alive_end_date")
    protected String aliveEndDate;

    @ApiModelProperty(name = "enable_status", value = "状态")
//    @JSONField(name = "enable_status")
    private Integer enableStatus;

    @ApiModelProperty(name = "source", value = "来源")
    @JSONField(name = "source")
    private Integer source;

    @ApiModelProperty(name = "maintain_status", value = "维护状态")
//    @JSONField(name = "maintain_status")
    private Integer maintainStatus;

    @JSONField(name = "assetSource")
    private Integer assetSource;

    @JSONField(name = "assetManage")
    private Integer assetManage;

    private Integer policy;
}
