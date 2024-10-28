package com.zans.mms.vo.devicepoint.subset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("分组中根据条件添加point实体")
public class PointSubsetDetailAddByConditionReqVO {

    @NotNull
    @ApiModelProperty(name = "subsetId", value = "分组id",required = true)
    private Long subsetId;

    @ApiModelProperty(name = "pointName", value = "点位名称",required = true)
    private String pointName;

    @ApiModelProperty(name = "areaId", value = "辖区id",required = true)
    private String areaId;

    @ApiModelProperty(name = "pointCode", value = "点位编号",required = true)
    private String pointCode;

    @ApiModelProperty(name = "deviceType", value = "设备类型",required = true)
    private String deviceType;

    @NotNull
    @ApiModelProperty(name = "1.强行添加点位(移除了其他子集中的点位，并添加到自己子集);2.忽略已存在的", value = "1.强行添加点位(移除了其他子集中的点位，并添加到自己子集);2.忽略已存在的",required = false)
    private Integer addType;

}
