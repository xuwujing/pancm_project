package com.zans.portal.vo.asset.branch.req;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("资产子集选择设备请求实体")
@Data
public class ChoiceDeviceReqVO extends BasePage {

    @NotNull
    @ApiModelProperty(name = "assetBranchId", value = "设备分组id",required = true)
    private Integer assetBranchId;

    @ApiModelProperty(name = "deviceType", value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(name = "ipAddr", value = "ip地址")
    private String ipAddr;

    @ApiModelProperty(name = "mac", value = "mac地址")
    private String mac;

    @ApiModelProperty(name = "pointName", value = "点位名称")
    private String pointName;

    @ApiModelProperty(name = "passList", value = "mac集合",hidden = true)
    private List<String> passList;

    @ApiModelProperty(name = "existAssetIds", value = "已存在的资产ip集合",hidden = true)
    private List<String> existAssetIds;
}
