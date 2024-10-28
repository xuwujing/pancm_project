package com.zans.portal.vo.switcher;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class SwitcherProcessorVO {

    private Integer id;

    @ApiModelProperty(name = "model_name", value = "型号全称", required = true)
    @JSONField(name = "model_name")
    private String modelName;

    @ApiModelProperty(name = "model_brand", value = "品牌", required = true)
    @JSONField(name = "model_brand")
    private String modelBrand;

    @ApiModelProperty(name = "model_desc", value = "具体型号", required = true)
    @JSONField(name = "model_desc")
    private String modelDesc;

    @ApiModelProperty(name = "sw_type", value = "交换机类型", required = true)
    @JSONField(name = "sw_type")
    private String swType;

    @ApiModelProperty(name = "module_name", value = "交换机处理模块", required = true)
    @JSONField(name = "module_name")
    private String moduleName;

    @ApiModelProperty(name = "class_name", value = "交换机处理类", required = true)
    @JSONField(name = "class_name")
    private String className;


}
