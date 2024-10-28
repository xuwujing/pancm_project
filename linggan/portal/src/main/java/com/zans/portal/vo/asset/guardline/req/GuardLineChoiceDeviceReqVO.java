package com.zans.portal.vo.asset.guardline.req;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.zans.base.vo.BasePage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class GuardLineChoiceDeviceReqVO extends BasePage {
    
    @NotNull
    @ApiModelProperty(name = "assetGuardLineId", value = "警卫线路id",required = true)
    private Integer assetGuardLineId;

    @ApiModelProperty(name = "deviceType", value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(name = "ipAddr", value = "ip地址")
    private String ipAddr;

    @ApiModelProperty(name = "username", value = "mac地址")
    private String username;

    @ApiModelProperty(name = "pointName", value = "点位名称")
    private String pointName;



    @ApiModelProperty(name = "passList", value = "mac集合",hidden = true)
    private List<String> passList;

    @ApiModelProperty(name = "existAssetIds", value = "已存在的资产id集合",hidden = true)
    private List<Integer> existAssetIds;

    
}
