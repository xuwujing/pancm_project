package com.zans.portal.vo.network;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * create by: beiming
 * create time: 2021/11/18 10:31
 */
@ApiModel
@Data
public class TopoSeqReqVO {
    @NotNull
    @ApiModelProperty(value = "sourceIps")
    private String sourceIp;

    @NotNull
    @ApiModelProperty(value = "destIps")
    private List<String> destIps;

    @NotNull
    @ApiModelProperty(value = "1,显示；2，隐藏")
    private Integer visible;
    @NotNull
    @ApiModelProperty(value = "拓扑结构值")
    private Integer topoDataType;

}
