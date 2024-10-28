package com.zans.portal.vo.asset.branch.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel
public class AssetBranchAddReqVO {
    @ApiModelProperty(name = "name", value = "分组名称",required = true)
    @NotEmpty
    @Length(max = 30)
    private String name;

    @ApiModelProperty(name = "level", value = "分组级别",hidden = true)
    private Integer level;

    @ApiModelProperty(name = "parentId", value = "父级id",hidden = true)
    private Integer parentId;

    @ApiModelProperty(name = "id", value = "分组id",hidden = true)
    private Integer id;

    @ApiModelProperty(name = "startName", value = "起点名称",hidden = true)
    private String startName;

    @ApiModelProperty(name = "endName", value = "终点名称",hidden = true)
    private String endName;

}
