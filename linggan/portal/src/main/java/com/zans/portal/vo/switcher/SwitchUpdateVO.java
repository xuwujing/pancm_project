package com.zans.portal.vo.switcher;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.portal.model.SysSwitcher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class SwitchUpdateVO {

    private Integer id;

    @ApiModelProperty(name = "sw_host", value = "ip", required = true)
    @JSONField(name = "sw_host")
    private String swHost;

    @ApiModelProperty(name = "name", value = "交换机名称")
    @JSONField(name = "name")
    private String name;

    @ApiModelProperty(name = "area", value = "区域")
    @JSONField(name = "area")
    private Integer area;

    @ApiModelProperty(name = "area_name", value = "区域")
    @JSONField(name = "area_name")
    private String areaName;

    @ApiModelProperty(name = "sw_type", value = "交换机类型")
    @JSONField(name = "sw_type")
    private Integer swType;

    @ApiModelProperty(name = "protocol", value = "管理协议")
    @JSONField(name = "protocol")
    private String protocol;

    @ApiModelProperty(name = "telnet_port", value = "telnet端口")
    @JSONField(name = "telnet_port")
    private Integer telnetPort;

    @ApiModelProperty(name = "ssh_port", value = "ssh端口")
    @JSONField(name = "ssh_port")
    private Integer sshPort;

    @ApiModelProperty(name = "sw_account", value = "登录账号")
    @JSONField(name = "sw_account")
    private String swAccount;

    @ApiModelProperty(name = "sw_password", value = "登录密码")
    @JSONField(name = "sw_password")
    private String swPassword;

    @ApiModelProperty(name = "sw_community", value = "SNMP团体名")
    @JSONField(name = "sw_community")
    private String swCommunity;

    @ApiModelProperty(name = "sw_snmp_version", value = "SNMP版本")
    @JSONField(name = "sw_snmp_version")
    private String swSnmpVersion;

    @ApiModelProperty(name = "radius_config", value = "Radius认证")
    @JSONField(name = "radius_config")
    private Integer radiusConfig;

    @ApiModelProperty(name = "brand", value = "品牌")
    @JSONField(name = "brand")
    private Integer brand;

    @ApiModelProperty(name = "brand_name", value = "品牌")
    @JSONField(name = "brand_name")
    private String brandName;

    @ApiModelProperty(name = "version", value = "软件版本")
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

    public static SysSwitcher initSwitch(SysSwitcher switcher, SwitchUpdateVO mergeVO) {
        switcher.setSwHost(mergeVO.getSwHost());
        switcher.setSwType(mergeVO.getSwType());
        switcher.setProtocol(mergeVO.getProtocol());
        switcher.setTelnetPort(mergeVO.getTelnetPort());
        switcher.setSshPort(mergeVO.getSshPort());
        switcher.setSwAccount(mergeVO.getSwAccount());
        switcher.setSwPassword(mergeVO.getSwPassword());
        switcher.setSwCommunity(mergeVO.getSwCommunity());
        switcher.setSwSnmpVersion(mergeVO.getSwSnmpVersion());
        switcher.setRadiusConfig(mergeVO.getRadiusConfig());
        switcher.setBrand(mergeVO.getBrand());
        switcher.setVersion(mergeVO.getVersion());
        switcher.setModel(mergeVO.getModel());
        return switcher;
    }


}
