package com.zans.portal.vo.asset.branch.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class AssetBranchEditReqVO {
    @NotNull
    @ApiModelProperty(name = "id", value = "分组id",required = true)
    private Integer id;

    @ApiModelProperty(name = "name", value = "分组名称")
    private String name;

}
