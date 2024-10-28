package com.zans.job.vo.node;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class NodeReqVO extends BasePage {

    @ApiModelProperty(name = "node_id", value = "应用名称")
    @JSONField(name = "node_id")
    private String nodeId;

    @ApiModelProperty(name = "ip", value = "ip")
    @JSONField(name = "ip")
    private String ip;

    @ApiModelProperty(name = "priority", value = "优先级")
    @JSONField(name = "priority")
    private Integer priority;

    @ApiModelProperty(name = "node_type", value = "服务器类型")
    @JSONField(name = "node_type")
    private String nodeType;

    @ApiModelProperty(name = "alive", value = "运行状态")
    @JSONField(name = "alive")
    private Integer alive;


}
