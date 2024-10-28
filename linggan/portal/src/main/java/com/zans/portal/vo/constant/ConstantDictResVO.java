package com.zans.portal.vo.constant;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class ConstantDictResVO {

    @ApiModelProperty(name = "dict_key", value = "常量类型主键")
    @JSONField(name = "dict_key")
    @NotNull
    private String dictKey;

    @ApiModelProperty(name = "dict_name", value = "常量类型名称")
    @JSONField(name = "dict_name")
    @NotNull
    private String dictName;

    @ApiModelProperty(name = "module_id", value = "模块id")
    @JSONField(name = "module_id")
    private Integer moduleId;

    @ApiModelProperty(name = "module_name", value = "模块名称")
    @JSONField(name = "module_name")
    private String moduleName;

    @ApiModelProperty(name = "table_name", value = "表名")
    @JSONField(name = "table_name")
    private String tableName;

    @ApiModelProperty(name = "column_name", value = "列名")
    @JSONField(name = "column_name")
    private String columnName;

}
