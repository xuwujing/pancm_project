package com.zans.mms.vo.devicepoint.map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
* @Title: DevicePointForMapResVO
* @Description: 点位地图 ResVO
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/22/21
*/
@ApiModel(value = "DevicePointForMapResVO", description = "")
@Data
public class DevicePointForMapResVO implements Serializable {
    @ApiModelProperty(value = "id")
    private Integer id ;

    @ApiModelProperty(value = "点位id")
    private Long pId;

    @ApiModelProperty(value = "设备类型ID")
    private String dType;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "点位纬度")
    private String longitude;

    @ApiModelProperty(value = "点位经度")
    private String latitude;

    @ApiModelProperty(value = "点位编号")
    private String pCode;

    @ApiModelProperty(value = "点位名称")
    private String pName;

}
