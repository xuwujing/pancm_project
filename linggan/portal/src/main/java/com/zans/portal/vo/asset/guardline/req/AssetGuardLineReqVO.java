package com.zans.portal.vo.asset.guardline.req;

import com.zans.base.vo.BasePage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AssetGuardLineReqVO extends BasePage {
    @ApiModelProperty(name = "assetGuardLineId", value = "线路id")
    private Integer assetGuardLineId;

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
