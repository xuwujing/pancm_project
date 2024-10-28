package com.zans.portal.vo.custom;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CustomDataRespVO {

    @ApiModelProperty(name = "id", value = "主键")
    @JSONField(name = "id")
    private Integer id;

    @ApiModelProperty(name = "rowid", value = "关联主表的rowid")
    @JSONField(name = "rowid")
    private String rowid;

    @ApiModelProperty(name = "field_id", value = "关联custom_field_id")
    @JSONField(name = "field_id")
    private Integer fieldId;

    @ApiModelProperty(name = "field_key", value = "列键")
    @JSONField(name = "field_key")
    private String fieldKey;

    @ApiModelProperty(name = "field_value", value = "列值")
    @JSONField(name = "field_value")
    private String fieldValue;


}
