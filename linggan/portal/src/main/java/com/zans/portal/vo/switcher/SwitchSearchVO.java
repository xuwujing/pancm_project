package com.zans.portal.vo.switcher;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class SwitchSearchVO  extends BasePage {


    @ApiModelProperty(name = "sw_type", value = "交换机类型")
    @JSONField(name = "sw_type")
    private Integer swType;

    @ApiModelProperty(name = "brand", value = "品牌")
    @JSONField(name = "brand")
    private Integer brand;

    @ApiModelProperty(name = "radius_config", value = "AAA认证")
    @JSONField(name = "radius_config")
    private Integer radiusConfig;

    @ApiModelProperty(name = "sw_host", value = "Ip")
    @JSONField(name = "sw_host")
    private String swHost;

    @ApiModelProperty(name = "remark", value = "点位名称")
    @JSONField(name = "remark")
    private String remark;

}
