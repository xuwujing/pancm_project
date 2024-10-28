package com.zans.mms.vo.ticket;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
* @Title: TicketCreateByAssetReqVO
* @Description: 资产子集一键工单请求VO
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/13/21
*/
@Data
@ApiModel(value = "TicketCreateByAssetReqVO", description = "资产子集一键工单请求VO")
public class TicketCreateByAssetReqVO implements Serializable {
    private static final long serialVersionUID = 1L;
//    @NotNull
//    @ApiModelProperty(value = "资产子集分组id")
//    private Long subsetId;

    @NotNull
    @ApiModelProperty(value = "资产id数组")
    private List<Long> assetIds;


    /**
     * 辖区id
     */
    @ApiModelProperty(value = "辖区id")
    private String areaId;
    /**
     * 设备类型
     */
    @NotNull
    @ApiModelProperty(value = "设备类型")
    private String deviceType;
    /**
     * 故障类型
     */
    @ApiModelProperty(value = "故障类型")
    private Integer issueType;
    /**
     * 故障级别
     */
    @NotNull
    @ApiModelProperty(value = "故障级别")
    private Integer issueLevel;
    /**
     * 故障来源
     */
    @NotNull
    @ApiModelProperty(value = "故障来源")
    private Integer issueSource;


    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String remark;
    /**
     * 创建用户
     */
    @ApiModelProperty(value = "创建用户")
    private String creator;

    /**
     * 分配单位代码
     */
    @NotNull
    @ApiModelProperty(value = "分配单位代码")
    private String allocDepartmentNum;


}
