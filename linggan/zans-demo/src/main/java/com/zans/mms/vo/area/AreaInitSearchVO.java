package com.zans.mms.vo.area;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class AreaInitSearchVO extends BasePage {

    @ApiModelProperty(name = "area", value = "区域名称")
    @JSONField(name = "area")
    private Integer area;

}
