package com.zans.portal.vo.network;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pancm
 * @Title: portal
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/10/26
 */
@ApiModel
@Data
public class NetworkArpChangeRespVO {

    private Integer id;


    @ApiModelProperty(name = "macAddr", value = "mac地址")
    private String macAddr;

    @ApiModelProperty(name = "oldMacAddr", value = "变化之前的MAC地址")
    private String oldMacAddr;

    @ApiModelProperty(name = "openPort", value = "开放端口")
    private String openPort;

    @ApiModelProperty(name = "oldOpenPort", value = "变化之前的开放端口列表")
    private String oldOpenPort;

    @ApiModelProperty(name = "modelDes", value = "设备型号")
    private String modelDes;

    @ApiModelProperty(name = "oldModelDes", value = "变化之前的设备型号")
    private String oldModelDes;

    @ApiModelProperty(name = "deviceType", value = "设备类型")
    private String deviceType;

    @ApiModelProperty(name = "oldDeviceType", value = "变化之前的设备类型")
    private String oldDeviceType;

    @ApiModelProperty(name = "company", value = "当前公司名称")
    private String company;

    @ApiModelProperty(name = "oldCompany", value = "公司名称")
    private String oldCompany;

    @ApiModelProperty(name = "updateTime", value = "扫描时间")
    private String updateTime;

    @ApiModelProperty(name = "areaName", value = "所属辖区")
    private String areaName;



}
