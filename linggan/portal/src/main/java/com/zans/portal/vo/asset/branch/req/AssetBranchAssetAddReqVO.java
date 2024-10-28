package com.zans.portal.vo.asset.branch.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("分组中添加资产实体")
public class AssetBranchAssetAddReqVO {
    /**
     *
     */
    @ApiModelProperty(name = "assetId", value = "资产id")
    private Integer assetId;

    @NotNull
    @ApiModelProperty(name = "ipAddr", value = "ip地址",required = true)
    private String ipAddr;

    @NotNull
    @ApiModelProperty(name = "assetBranchId", value = "分组id",required = true)
    private Integer assetBranchId;
}
