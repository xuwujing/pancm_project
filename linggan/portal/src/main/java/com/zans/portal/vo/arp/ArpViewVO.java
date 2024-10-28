package com.zans.portal.vo.arp;

import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class ArpViewVO  extends BasePage {

    @ApiModelProperty(name = "id",value = "设备ID", required = true)
    @NotNull(message = "设备ID必填")
    private Integer id;

}
