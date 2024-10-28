package com.zans.portal.vo.mac;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class MacVO extends BasePage {

    @ApiModelProperty(name = "mac_addr", value = "mac地址")
    @JSONField(name = "mac_addr")
    @NotNull
    private String macAddr;

    @ApiModelProperty(name = "company", value = "厂商")
    @JSONField(name = "company")
    @NotNull
    private String company;

}
