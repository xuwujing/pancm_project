package com.zans.portal.vo.asset.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

@ApiModel
@Data
public class SeqReqVO {
    @ApiModelProperty(name = "id", value = "id",required = true)
    @NonNull
    private Integer id;
    @ApiModelProperty(name = "seq", value = "顺序",required = true)
    @NonNull
    private Integer seq;
}
