package com.zans.mms.vo.alert;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AlertTypeRespVO {

    @ApiModelProperty(name = "id",value = "策略类型id")
    private Long id;

    @ApiModelProperty(name = "typeName",value = "策略类型名称")
    private String typeName;

    @ApiModelProperty(name = "desc",value = "策略类型描述")
    private String desc;

    @ApiModelProperty(name = "status",value = "策略状态")
    private Integer status;

    @ApiModelProperty(name = "createUser",value = "创建人")
    private String createUser;

    @ApiModelProperty(name = "createTime",value = "创建时间")
    private String createTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
