package com.zans.portal.vo.asset.map;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@ApiModel
@Data
public class AssetMapInitReqVO {

    @NotNull(message = "leftLatitude不能为空")
    @ApiModelProperty(name = "leftLatitude",value = "左下角（西南角）纬度",required = true)
    private BigDecimal leftLatitude;
    @NotNull(message = "leftLongitude不能为空")
    @ApiModelProperty(name = "leftLongitude",value = "左下角（西南角）经度",required = true)
    private BigDecimal leftLongitude;
    @NotNull(message = "rightLatitude不能为空")
    @ApiModelProperty(name = "rightLatitude",value = "右上角（东北角）纬度",required = true)
    private BigDecimal rightLatitude;
    @NotNull(message = "rightLongitude不能为空")
    @ApiModelProperty(name = "rightLongitude",value = "右上角（东北角）经度",required = true)
    private BigDecimal rightLongitude;
    @NotNull(message = "zoomLevel不能为空")
    @ApiModelProperty(name = "zoomLevel",value = "地图缩放级别",required = true)
    private Integer zoomLevel;

    @ApiModelProperty(name = "buildTypes", value = "海康=1期  广电 1; 其他二期  电信 2",hidden = true)
    private List<String> buildTypes;

    @ApiModelProperty(name = "brandNameIsNoHaiKang", value = "海康=1期  广电 1; 其他二期  电信 2",hidden = true)
    private String brandNameIsNoHaiKang;

    @ApiModelProperty(name = "brand_name", value = "名牌",hidden = true)
    private String brandName;



}
