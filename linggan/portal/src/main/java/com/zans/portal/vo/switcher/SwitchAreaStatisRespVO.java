package com.zans.portal.vo.switcher;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 交换机区域/辖区统计结果
 */
@ApiModel
@Data
public class SwitchAreaStatisRespVO {
    
    @ApiModelProperty(name = "areaName", value = "区域/辖区")
    private String areaName;

    @ApiModelProperty(name = "totalNum", value = "交换机数量")
    private Long totalNum = 0L;

    @ApiModelProperty(name = "aliveNum", value = "在线交换机数量")
    private Long aliveNum = 0L;

    @ApiModelProperty(name = "onlineRate", value = "交换机在线率")
    private Double onlineRate = 0D;

}
