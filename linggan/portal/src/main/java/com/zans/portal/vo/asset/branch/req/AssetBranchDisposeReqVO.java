package com.zans.portal.vo.asset.branch.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("资产处置请求体")
public class AssetBranchDisposeReqVO {
    /**
     * 用户名
     */
    @NotEmpty
    @ApiModelProperty(name = "ipAddr", value = "ipAddr",required = true)
    private String ipAddr;

    /**
     * 停用理由
     */
    @NotEmpty
    @ApiModelProperty(name = "remark", value = "停用理由",required = true)
    private String remark;

    /**
     * 0，停用;1,启用
     */
    @NotNull
    @ApiModelProperty(name = "enableStatus", value = "0，停用;1,启用",required = true)
    private Integer enableStatus;


    @ApiModelProperty(name = "updateId", value = "修改人",hidden = true)
    private Integer updateId;
}
