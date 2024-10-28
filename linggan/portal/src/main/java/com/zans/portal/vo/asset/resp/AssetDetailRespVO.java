package com.zans.portal.vo.asset.resp;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资产详情
 */
@ApiModel
@Data
public class AssetDetailRespVO {
    
    @ApiModelProperty(name = "id" ,value = "唯一主键")
    private Integer id;

    @ApiModelProperty(name = "pointName",value = "点位名称")
    private String pointName;

    @ApiModelProperty(name = "deviceType",value = "设备类型ID")
    private Integer deviceType;

    @ApiModelProperty(name = "deviceTypeName",value = "设备类型名称")
    private String deviceTypeName;


    @ApiModelProperty(name = "areaName",value = "所属辖区名称")
    private String areaName;

    @ApiModelProperty(name = "deviceModelBrand",value = "设备品牌")
    private String deviceModelBrand;

    @ApiModelProperty(name = "deviceModelDes",value = "设备型号")
    private String deviceModelDes;

    @ApiModelProperty(name = "ipAddr",value = "IP地址")
    private String ipAddr;

    @ApiModelProperty(name = "mute",value = "是否哑终端")
    private Integer mute;

    @ApiModelProperty(name = "muteName",value = "是否哑终端名称")
    private String muteName;

    @ApiModelProperty(name = "projectName",value = "项目名称")
    private String projectName;

    @ApiModelProperty(name = "contractor",value = "承建单位")
    private String contractor;

    @ApiModelProperty(name = "contractorPerson",value = "承建联系人")
    private String contractorPerson;

    @ApiModelProperty(name = "contractorPhone",value = "承建联系人")
    private String contractorPhone;

    @ApiModelProperty(name = "maintainCompany",value = "维护单位")
    private String maintainCompany;

    @ApiModelProperty(name = "maintainPerson",value = "维护联系人")
    private String maintainPerson;

    @ApiModelProperty(name = "maintainPhone",value = "维护联系电话")
    private String maintainPhone;

    @ApiModelProperty(name = "maintainStatus",value = "维护状态")
    private Integer maintainStatus;

    @ApiModelProperty(name = "longitude",value = "经度")
    private String longitude;

    @ApiModelProperty(name = "latitude",value = "纬度")
    private String latitude;

    public void setMuteByMap(Map<Object, String> map) {
        Integer status = this.getMute();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setMuteName(name);
    }
}
