package com.zans.portal.vo.common;

import com.zans.base.util.ArithmeticUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统计数量和占有率
 */
@ApiModel
@Data
public class ChartStatisVO {
    
    @ApiModelProperty(name = "name", value = "显示名称")
    private String name;

    @ApiModelProperty(name = "totalNum", value = "总数量")
    private Long totalNum;

    @ApiModelProperty(name = "count", value = "数量")
    private Long count = 0L;
    @ApiModelProperty(name = "value", value = "兼容count数量")
    private Long value = 0L;

    @ApiModelProperty(name = "occupancyRate", value = "率")
    private Double occupancyRate = 0D;

    public Double getOccupancyRate() {
        if (totalNum != null && totalNum > 0){
           return ArithmeticUtil.percent(count, totalNum, 1);
        }
        return occupancyRate;
    }

    public Long getValue() {
        return count;
    }
}
