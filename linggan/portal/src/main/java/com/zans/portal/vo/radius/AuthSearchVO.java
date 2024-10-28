package com.zans.portal.vo.radius;

import com.zans.base.vo.MacPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AuthSearchVO extends MacPage {

    @ApiModelProperty(name = "company", value = "设备厂商")
    private String company;

    @ApiModelProperty(name = "area", value = "区域")
    private Integer area;

    @ApiModelProperty(name = "port", value = "接入端口")
    private String port;
}
