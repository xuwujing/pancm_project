package com.zans.mms.vo.radius;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.vo.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ServerSearchVO extends BasePage {

    @ApiModelProperty(name = "server_name", value = "服务名称")
    @JSONField(name = "server_name")
    private String serverName;

    @ApiModelProperty(name = "service_host", value = "服务IP地址")
    @JSONField(name = "service_host")
    private String serviceHost;

    @ApiModelProperty(name = "sql_host", value = "数据库ip")
    @JSONField(name = "sql_host")
    private String sqlHost;

    @ApiModelProperty(name = "enable", value = "是否启用 1.启用；0.禁用")
    @JSONField(name = "enable")
    private Integer enable;

    @ApiModelProperty(name = "alive", value = "能否访问 1.启用；0.不可用")
    @JSONField(name = "alive")
    private Integer alive;

}
