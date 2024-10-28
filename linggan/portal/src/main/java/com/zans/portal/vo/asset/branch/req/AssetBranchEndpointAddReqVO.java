package com.zans.portal.vo.asset.branch.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("分组中添加资产实体")
public class AssetBranchEndpointAddReqVO {
    /**
     * 用户名
     */
    @NotNull
    @ApiModelProperty(name = "username", value = "username",required = true)
    private String username;

    /**
     * 12位
     */
    @NotNull
    @ApiModelProperty(name = "pass", value = "pass",required = true)
    private String pass;

    @NotNull
    @ApiModelProperty(name = "assetBranchId", value = "分组id",required = true)
    private Integer assetBranchId;
}
