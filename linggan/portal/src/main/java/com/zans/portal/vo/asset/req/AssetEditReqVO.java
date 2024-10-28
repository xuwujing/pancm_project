package com.zans.portal.vo.asset.req;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 编辑资产所需信息
 */
@ApiModel
@Data
public class AssetEditReqVO extends AssetAddReqVO{
    
    @ApiModelProperty(name = "id" ,value = "唯一主键")
    @NotNull(message = "ID不能为空")
    private Integer id;

}
