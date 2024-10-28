package com.zans.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "SysUserBatchEditReqVO", description = "")
@Data
public class SysUserBatchEditReqVO {
    @NotNull
    @ApiModelProperty(value = "ids集合")
    private List<Integer> ids;

    @ApiModelProperty(value = "1,启用；0，禁用; 2,锁定;")
    private Integer enableStatus;

    @ApiModelProperty(value = "1.刪除")
    private Integer deleteFlag;
}
