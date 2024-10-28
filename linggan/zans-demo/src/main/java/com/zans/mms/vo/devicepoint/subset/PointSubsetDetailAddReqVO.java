package com.zans.mms.vo.devicepoint.subset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@ApiModel("分组中添加point实体")
public class PointSubsetDetailAddReqVO {
    /**
     *
     */
    @NotNull
    @ApiModelProperty(name = "pointIds", value = "pointIds 编号",required = true)
    private Set<Long> pointIds;


    @NotNull
    @ApiModelProperty(name = "subsetId", value = "分组id",required = true)
    private Long subsetId;
    @NotNull
    @ApiModelProperty(name = "1.强行添加点位(移除了其他子集中的点位，并添加到自己子集);2.忽略已存在的", value = "1.强行添加点位(移除了其他子集中的点位，并添加到自己子集);2.忽略已存在的",required = false)
    private Integer addType;


}
