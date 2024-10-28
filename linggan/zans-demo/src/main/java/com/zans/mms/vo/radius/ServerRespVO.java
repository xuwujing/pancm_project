package com.zans.mms.vo.radius;


import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class ServerRespVO {

    @ApiModelProperty(name = "server_id", value = "id", required = true)
    @JSONField(name = "server_id")
    private Integer serverId;

    @ApiModelProperty(name = "server_name", value = "server名称", required = true)
    @NotNull
    @JSONField(name = "server_name")
    private String serverName;

    @ApiModelProperty(name = "server_desc", value = "server描述")
    @JSONField(name = "server_desc")
    private String serverDesc;

    @ApiModelProperty(name = "sql_driver", value = "数据库类型，mysql|oracle", required = true)
    @JSONField(name = "sql_driver")
    @NotNull
    private String sqlDriver;

    @ApiModelProperty(name = "sql_host", value = "主机IP", required = true)
    @JSONField(name = "sql_host")
    @NotNull
    private String sqlHost;

    @ApiModelProperty(name = "sql_port", value = "端口", required = true)
    @JSONField(name = "sql_port")
    @NotNull
    private Integer sqlPort;

    @ApiModelProperty(name = "sql_user", value = "账号", required = true)
    @JSONField(name = "sql_user")
    @NotNull
    private String sqlUser;

    @ApiModelProperty(name = "sql_passwd", value = "密码", required = true)
    @JSONField(name = "sql_passwd")
    @NotNull
    private String sqlPasswd;

    @ApiModelProperty(name = "sql_db", value = "数据库名称", required = true)
    @JSONField(name = "sql_db")
    @NotNull
    private String sqlDb;

    @ApiModelProperty(name = "enable", value = "是否启用", required = true)
    @JSONField(name = "enable")
    @NotNull
    private Integer enable;

    @ApiModelProperty(name = "alive", value = "能否访问")
    @JSONField(name = "alive")
    private Integer alive;

    @ApiModelProperty(name = "service_host", value = "服务IP地址", required = true)
    @JSONField(name = "service_host")
    @NotNull
    private String serviceHost;

    @ApiModelProperty(name = "service_auth_port", value = "服务认证端口", required = true)
    @JSONField(name = "service_auth_port")
    @NotNull
    private Integer serviceAuthPort;

    @ApiModelProperty(name = "service_accounting_port", value = "服务计费端口", required = true)
    @JSONField(name = "service_accounting_port")
    @NotNull
    private Integer serviceAccountingPort;

    @ApiModelProperty(name = "service_secret", value = "服务密钥", required = true)
    @JSONField(name = "service_secret")
    @NotNull
    private String serviceSecret;

    @ApiModelProperty(name = "test_type", value = "测试类型：0-数据库测试，1-认证测试，2-计费测试", required = true)
    @JSONField(name = "test_type")
    private Integer testType;


}
