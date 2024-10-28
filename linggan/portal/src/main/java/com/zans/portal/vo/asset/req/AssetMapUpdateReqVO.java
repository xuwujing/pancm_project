package com.zans.portal.vo.asset.req;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class AssetMapUpdateReqVO {

    @JSONField(name = "id")
    @ApiModelProperty(name = "id", value = "id",required = true)
    @NotNull
    private Integer id;

//    @JSONField(name = "department_id")
    @ApiModelProperty(name = "department_id", value = "部门id")
    private Integer departmentId;

    @JSONField(name = "longitude")
    @ApiModelProperty(name = "longitude", value = "经度",required = true)
    @NotNull
    private String longitude;

    @JSONField(name = "latitude")
    @ApiModelProperty(name = "latitude", value = "纬度",required = true)
    @NotNull
    private String latitude;
}
