package com.zans.portal.vo.switcher;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class SwitchMergeVO {

    private Integer id;

    @ApiModelProperty(name = "sw_host", value = "ip", required = true)
    @NotNull(message = "ip")
    @JSONField(name = "sw_host")
    private String swHost;

    @ApiModelProperty(name = "name", value = "交换机名称")
    @NotNull(message = "交换机名称")
    @JSONField(name = "name")
    private String name;


    @ApiModelProperty(name = "sw_type", value = "交换机类型")
    @NotNull(message = "交换机类型")
    @JSONField(name = "sw_type")
    private Integer swType;

    @ApiModelProperty(name = "protocol", value = "管理协议")
    @NotNull(message = "管理协议")
    @JSONField(name = "protocol")
    private String protocol;

    @ApiModelProperty(name = "telnet_port", value = "telnet端口")
    @JSONField(name = "telnet_port")
    private Integer telnetPort;

    @ApiModelProperty(name = "ssh_port", value = "ssh端口")
    @JSONField(name = "ssh_port")
    private Integer sshPort;

    @ApiModelProperty(name = "sw_account", value = "登录账号")
    @NotNull(message = "登录账号")
    @JSONField(name = "sw_account")
    private String swAccount;

    @ApiModelProperty(name = "sw_password", value = "登录密码")
    @NotNull(message = "登录密码")
    @JSONField(name = "sw_password")
    private String swPassword;

    @ApiModelProperty(name = "sw_community", value = "SNMP团体名")
    @JSONField(name = "sw_community")
    private String swCommunity;

    @ApiModelProperty(name = "sw_snmp_version", value = "SNMP版本")
    @JSONField(name = "sw_snmp_version")
    private String swSnmpVersion;

    @ApiModelProperty(name = "radius_config", value = "Radius认证")
    @NotNull(message = "Radius认证")
    @JSONField(name = "radius_config")
    private Integer radiusConfig;

    @ApiModelProperty(name = "brand", value = "品牌")
    @JSONField(name = "brand")
    private Integer brand;

    @ApiModelProperty(name = "version", value = "软件版本")
    @JSONField(name = "version")
    private String version;

    @ApiModelProperty(name = "model", value = "处理器")
    @JSONField(name = "model")
    private String model;

    @ApiModelProperty(name = "nas_ip", value = "nas ip")
    @JSONField(name = "nas_ip")
    private String nasIp;

    @ApiModelProperty(name = "secret", value = "nas密码")
    @JSONField(name = "secret")
    private String secret;

    @ApiModelProperty(name = "short_name", value = "nas简称")
    @JSONField(name = "short_name")
    private String shortName;

    @ApiModelProperty(name = "nas_type", value = "nas类型")
    @JSONField(name = "nas_type")
    private String nasType;

    @ApiModelProperty(name = "sys_desc", value = "型号")
    @JSONField(name = "sys_desc")
    private String sysDesc;

    // 1:ssh   2:telnet  3:snmp  4:AAA 5:COA/DM认证
    @ApiModelProperty(name = "test_type", value = "测试类型")
    @JSONField(name = "test_type")
    private Integer testType;

}
