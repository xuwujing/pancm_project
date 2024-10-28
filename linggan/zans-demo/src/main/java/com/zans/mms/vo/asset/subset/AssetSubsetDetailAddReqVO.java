package com.zans.mms.vo.asset.subset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@ApiModel("分组中添加资产实体")
public class AssetSubsetDetailAddReqVO {
    /**
     *
     */
    @NotNull
    @ApiModelProperty(name = "assetId", value = "assetId 资产编号",required = true)
    private Set<Long> assetIds;


    @NotNull
    @ApiModelProperty(name = "subsetId", value = "分组id",required = true)
    private Long subsetId;
}
