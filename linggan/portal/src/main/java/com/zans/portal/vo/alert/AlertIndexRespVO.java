package com.zans.portal.vo.alert;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AlertIndexRespVO {


    @ApiModelProperty(name = "indexId",value = "指标id")
    private Long indexId;

    @ApiModelProperty(name = "typeName",value = "告警类型名称")
    private String typeName;

    @ApiModelProperty(name = "typeId",value = "告警类型ID")
    private String typeId;

    @ApiModelProperty(name = "indexName",value = "指标名称")
    private String indexName;

    @ApiModelProperty(name = "desc",value = "描述")
    private String desc;

    @ApiModelProperty(name = "status",value = "状态")
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
