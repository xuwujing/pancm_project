package com.zans.mms.vo.constant;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class ConstantItemSearchVO extends BasePage {

    @ApiModelProperty(name = "dict_name", value = "常量的类型")
    @JSONField(name = "dict_name")
    private String dictName;

    @ApiModelProperty(name = "module_id", value = "模块id")
    @JSONField(name = "module_id")
    private String moduleId;

    @ApiModelProperty(name = "dict_key", value = "key", required = true)
    @JSONField(name = "dict_key")
    @NotNull
    private String dictKey;

    @ApiModelProperty(name = "table_name", value = "表名")
    @JSONField(name = "table_name")
    private String tableName;

    @ApiModelProperty(name = "column_name", value = "列名")
    @JSONField(name = "column_name")
    private String columnName;

}
