package com.zans.mms.vo.devicepoint.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
* @Title: DevicePointCheckReqVO
* @Description: 点位表校正ReqVO
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/12/21
*/

@ApiModel(value = "DevicePointCheckReqVO", description = "")
@Data
public class DevicePointCheckReqVO implements Serializable {
    @NotNull
    @ApiModelProperty(value = "pointId",required = true)
    private Long pointId;


    @NotBlank
    @ApiModelProperty(value = "经度",name = "GCJ02 经度",required = true)
    private String longitude;


    @NotBlank
    @ApiModelProperty(value = "维度",name = "GCJ02 维度",required = true)
    private String latitude;

    @ApiModelProperty(value = "creator",name = "creator",hidden = true)
    private String creator;

    @ApiModelProperty(value = "prevLongitude",name = "GCJ02 经度",hidden = true)
    private String prevLongitude;

    @ApiModelProperty(value = "prevLatitude",name = "GCJ02 维度",hidden = true)
    private String prevLatitude;

}
