package com.zans.mms.vo.asset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
* @Title: AssetForMapResVO
* @Description: 资产地图 ResVO
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/22/21
*/
@ApiModel(value = "AssetForMapResVO", description = "")
@Data
public class AssetForMapResVO implements Serializable {
    @ApiModelProperty(value = "id")
    private Integer id ;

    @ApiModelProperty(value = "资产id")
    private Long aId;

    @ApiModelProperty(value = "点位id")
    private Long pId;

    @ApiModelProperty(value = "设备类型ID")
    private String dType;

    @ApiModelProperty(value = "点位纬度")
    private String longitude;

    @ApiModelProperty(value = "点位经度")
    private String latitude;

    @ApiModelProperty(value = "在线状态")
    private String onlineStatus;



}
