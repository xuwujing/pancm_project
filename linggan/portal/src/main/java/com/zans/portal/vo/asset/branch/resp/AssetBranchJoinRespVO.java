package com.zans.portal.vo.asset.branch.resp;

import com.zans.portal.model.AssetBranch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AssetBranchJoinRespVO {
    @ApiModelProperty(value = "当前节点")
    private AssetBranch slef;

    @ApiModelProperty(value = "后一个节点")
    private AssetBranch next;

    @ApiModelProperty(value = "前一个节点")
    private AssetBranch prev;
}
