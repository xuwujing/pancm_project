package com.zans.portal.vo.switcher;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class SwitchDetailVO {

    private Integer id;

    @ApiModelProperty(name = "sw_host", value = "ip", required = true)
    @NotNull(message = "ip")
    @JSONField(name = "sw_host")
    private String swHost;

    @ApiModelProperty(name = "name", value = "交换机名称")
    @NotNull(message = "交换机名称")
    @JSONField(name = "name")
    private String name;

    @ApiModelProperty(name = "area", value = "区域")
    @NotNull(message = "区域")
    @JSONField(name = "area")
    private Integer area;

    @ApiModelProperty(name = "area_name", value = "区域")
    @JSONField(name = "area_name")
    private String areaName;

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

    @ApiModelProperty(name = "brand_name", value = "品牌")
    @JSONField(name = "brand_name")
    private String brandName;

    @ApiModelProperty(name = "version", value = "型号")
    @JSONField(name = "version")
    private String version;

    @ApiModelProperty(name = "model", value = "处理器")
    @JSONField(name = "model")
    private String model;

    @ApiModelProperty(name = "scanRespVO", value = "scan")
    @JSONField(name = "scanRespVO")
    private SwitcherScanRespVO scanRespVO;

    @ApiModelProperty(name = "remark", value = "scan")
    @JSONField(name = "remark")
    private String remark;

    @JSONField(name = "scan_interface_count")
    private Integer scanInterfaceCount;

    @JSONField(name = "scan_mac_alive")
    private Integer scanMacAlive;

    @JSONField(name = "scan_mac_all")
    private Integer scanMacAll;

    @ApiModelProperty(name = "sys_desc", value = "型号")
    @JSONField(name = "sys_desc")
    private String sysDesc;

    @ApiModelProperty(name = "lon", value = "纬度")
    private BigDecimal lon;
    @ApiModelProperty(name = "lat", value = "经度")
    private BigDecimal lat;

    @ApiModelProperty(name = "projectName", value = "项目名称")
    private String projectName;
    @ApiModelProperty(name = "acceptance", value = "状态 0未验收 1 已验收")

    private Integer acceptance;
    @ApiModelProperty(name = "acceptDate", value = "验收时间格式 yyyy-MM-dd")
    private String acceptDate;
    @ApiModelProperty(name = "acceptIdea", value = "验收意见")
    private String acceptIdea;//

    @ApiModelProperty(name = "pointName", value = "点位名称")
    private String pointName;

    @ApiModelProperty(name = "areaNameD", value = "大队名称")
    private String areaNameD;

    @ApiModelProperty(name = "regionName", value = "区域")
    private String regionName;

    @ApiModelProperty(name = "swTypeName", value = "交换机类型名称")
    private String swTypeName;

    @ApiModelProperty(name = "sysName", value = "交换机名称")
    private String sysName;

    @ApiModelProperty(name = "ipAddr", value = "ip")
    private String ipAddr;

    @ApiModelProperty(name = "status", value = "状态 '0启用 1 停用" )
    private Integer status;

    @ApiModelProperty(name = "alive", value = "在线状态 1:在线；2；不在线")
    private Integer alive;

    @ApiModelProperty(name = "community", value = "snmp名称")
    private String community;
    @ApiModelProperty(name = "consBatch", value = "建设批次1 一期 2二期")
    private Integer consBatch;//
    public String getConsBatchCn() {
        if (consBatch != null){
            if (consBatch ==1){
                return "一期";
            }else {
                return "二期";
            }
        }
        return null;
    }

}
