package com.zans.portal.vo.switcher;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel
@Data
public class SwitchBranchMapUpdateVO {

    @ApiModelProperty(name = "id", value = "主键id")
    private Integer id;

    @ApiModelProperty(name = "longitude", value = "经度", required = true)
    @NotNull(message = "经度")
    private BigDecimal longitude;
    @ApiModelProperty(name = "latitude", value = "纬度", required = true)
    @NotNull(message = "纬度")
    private BigDecimal latitude;




}
