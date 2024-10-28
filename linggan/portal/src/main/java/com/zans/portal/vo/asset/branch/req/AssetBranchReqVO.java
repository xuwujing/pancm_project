package com.zans.portal.vo.asset.branch.req;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("运行统计详情请求实体")
@Data
public class AssetBranchReqVO extends BasePage {

    @ApiModelProperty(name = "AssetBranchId", value = "设备分组id")
    private Integer AssetBranchId;

    @ApiModelProperty(name = "alive", value = "1.在线；2.离线")
    private Integer alive;

    @ApiModelProperty(name = "pointName", value = "点位")
    private String pointName;

    @ApiModelProperty(name = "deviceType", value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(name = "ipAddr", value = "ip地址")
    private String ipAddr;

    @ApiModelProperty(name = "enableStatus", value = "0，停用;1,启用")
    private Integer enableStatus;
}