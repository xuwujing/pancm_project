package com.zans.portal.vo.asset.guardline.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AssetGuardLineRespVO {

    @ApiModelProperty(name = "id", value = "id")
    private Integer id;

    @ApiModelProperty(name = "name", value = "警卫线路名称")
    private String name;

    @ApiModelProperty(name = "enableFastScan", value = "是否启用快速扫描,0未启用,1启用")
    private Integer enableFastScan;

    @ApiModelProperty(name = "seq", value = "排序")
    private Integer seq;
}
