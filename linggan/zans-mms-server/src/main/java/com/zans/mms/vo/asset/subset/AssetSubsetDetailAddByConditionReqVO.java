package com.zans.mms.vo.asset.subset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("分组中根据条件添加资产实体")
public class AssetSubsetDetailAddByConditionReqVO {

    @NotNull
    @ApiModelProperty(name = "subsetId", value = "分组id",required = true)
    private Long subsetId;

    @ApiModelProperty(name = "pointName", value = "点位名称",required = true)
    private String pointName;

    @ApiModelProperty(name = "maintainStatus", value = "点位状态",required = true)
    private List<String> maintainStatus;

    @ApiModelProperty(name = "areaId", value = "辖区id",required = true)
    private List<String> areaId;

    @ApiModelProperty(name = "pointCode", value = "点位编码",required = true)
    private String pointCode;

    @ApiModelProperty(name = "assetCode", value = "设备编码",required = true)
    private String assetCode;

    @ApiModelProperty(name = "deviceType", value = "设备类型",required = true)
    private String deviceType;


    /**
     * 所属单位id
     */
    private List<String> orgId;

    //新加过滤字段
    private List<Integer> projectIdList;


    private String buildCompany;

    /**
     * 项目列表过滤
     */
    private List<Integer> itemList;

}
