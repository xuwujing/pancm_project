package com.zans.portal.vo.asset.guardline.resp;

import com.zans.portal.model.AssetGuardLine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AssetGuardLineJoinRespVO {
    @ApiModelProperty(value = "当前节点")
    private AssetGuardLine slef;

    @ApiModelProperty(value = "后一个节点")
    private AssetGuardLine next;

    @ApiModelProperty(value = "前一个节点")
    private AssetGuardLine prev;
}
