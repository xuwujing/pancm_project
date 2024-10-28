package com.zans.portal.vo.switcher;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@ApiModel
@Data
public class SwitchBranchMapInitVO {

    @NotNull
    @ApiModelProperty(name = "leftLatitude",value = "左下角（西南角）纬度",required = true)
    private BigDecimal leftLatitude;
    @NotNull
    @ApiModelProperty(name = "leftLongitude",value = "左下角（西南角）经度",required = true)
    private BigDecimal leftLongitude;
    @NotNull
    @ApiModelProperty(name = "rightLatitude",value = "右上角（东北角）纬度",required = true)
    private BigDecimal rightLatitude;
    @NotNull
    @ApiModelProperty(name = "rightLongitude",value = "右上角（东北角）经度",required = true)
    private BigDecimal rightLongitude;
    @NotNull
    @ApiModelProperty(name = "zoomLevel",value = "地图缩放级别",required = true)
    private Integer zoomLevel;


    @ApiModelProperty(name = "projectIds",value = "项目ids")
    private List<Integer> projectIds;

    @ApiModelProperty(name = "projectName",value = "项目名称")
    public String projectName;

    @ApiModelProperty(name = "pointName",value = "点位名称名称")
    public String pointName;

    public String getPointName() {
        if (pointName != null){
            return pointName.replace(" ","");
        }
        return pointName;
    }
}
