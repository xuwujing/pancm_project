package com.zans.portal.vo.asset.guardline.req;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("线路中添加资产实体")
public class AssetGuardLineAssetAddReqVO {
    @NotNull
    @ApiModelProperty(name = "assetId", value = "资产id",required = true)
    private Integer assetId;

    @NotNull
    @ApiModelProperty(name = "ipAddr", value = "ip地址",required = true)
    private String ipAddr;

    @NotNull
    @ApiModelProperty(name = "guardLineId", value = "线路id",required = true)
    private Integer guardLineId;
}
