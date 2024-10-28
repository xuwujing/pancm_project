package com.zans.portal.vo.alert.offlineDevice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class OfflineDeviceDisposeVO {


    @ApiModelProperty(name = "alertIds", value = "alertIds")
    private List<Integer> alertIds;


    @ApiModelProperty(name = "enableStatus", value = "状态  0，停用;1,启用")
    @NotNull(message = "状态")
    private Integer enableStatus;

    @ApiModelProperty(name = "reason", value = "处置原因")
    @NotNull(message = "处置原因")
    private String reason;




}
