package com.zans.portal.vo.asset.branch.resp;

import com.zans.base.office.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description="资产子集运行统计excel")
@Data
public class ExcelAssetRunStatisticsVO {

    @ApiModelProperty(name = "assetBranchName", value = "设备分组名")
    @ExcelProperty(value = "设备组名称", index = 1, width = 15)
    private String  assetBranchName;

    @ApiModelProperty(name = "branchTotal", value = "设备总数")
    @ExcelProperty(value = "设备总数", index = 1, width = 15)
    private Integer  branchTotal;

    @ApiModelProperty(name = "onlineNumber", value = "在线设备")
    @ExcelProperty(value = "在线设备", index = 1, width = 15)
    private Integer  onlineNumber;

    @ApiModelProperty(name = "offlineNumber", value = "离线设备")
    @ExcelProperty(value = "离线设备", index = 1, width = 15)
    private Integer  offlineNumber;

    @ApiModelProperty(name = "disableNumber", value = "停用设备")
    @ExcelProperty(value = "停用设备", index = 1, width = 15)
    private Integer  disableNumber;

    @ApiModelProperty(name = "onlineRate", value = "在线率")
    @ExcelProperty(value = "在线率", index = 1, width = 15)
    private String  onlineRate;
}
