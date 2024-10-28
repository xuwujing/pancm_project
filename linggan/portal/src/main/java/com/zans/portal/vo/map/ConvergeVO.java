package com.zans.portal.vo.map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "汇聚点VO")
@Data
public class ConvergeVO {

    @ApiModelProperty(name = "latitude",value = "汇聚点经度")
    private String latitude;

    @ApiModelProperty(name = "longitude",value = "汇聚点纬度")
    private String longitude;

    @ApiModelProperty(name = "convergeNumber",value = "汇聚点数量")
    private Integer convergeNumber;



    @ApiModelProperty(name = "leftLatitude",value = "左下角（西南角）经度")
    private String leftLatitude;
    @ApiModelProperty(name = "leftLongitude",value = "左下角（西南角）纬度")
    private String leftLongitude;
    @ApiModelProperty(name = "rightLatitude",value = "右上角（东北角）经度")
    private String rightLatitude;
    @ApiModelProperty(name = "rightLongitude",value = "右上角（东北角）纬度")
    private String rightLongitude;

}
