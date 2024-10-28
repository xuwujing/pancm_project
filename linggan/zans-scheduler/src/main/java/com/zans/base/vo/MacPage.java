package com.zans.base.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xv
 * @since 2020/4/16 17:55
 */
@Data
public class MacPage extends BasePage{

    @ApiModelProperty(value = "MAC地址")
    protected String mac;
}
