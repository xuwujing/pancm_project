package com.zans.portal.vo.constant;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ConstantDictSearchVO extends BasePage {

    @ApiModelProperty(name = "module_id", value = "模块id", required = true)
    @JSONField(name = "module_id")
    private String moduleId;

    @ApiModelProperty(name = "dict_name", value = "字典名称", required = true)
    @JSONField(name = "dict_name")
    private String dictName;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }
}
