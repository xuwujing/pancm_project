package com.zans.mms.vo.deny;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.mms.vo.arp.AssetRespVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel
@Data
public class DenyVO {

    private Integer id;

    @ApiModelProperty(name = "risk_type", value = "设备变动")
    @JSONField(name = "risk_type")
    private String riskType = "异常";

    @ApiModelProperty(name = "deny_reason_int", value = "设备变动")
    @JSONField(name = "deny_reason_int")
    private Integer denyReasonInt;

    @ApiModelProperty(name = "deny_time", value = "异常时间")
    @JSONField(name = "deny_time", format = "yyyy-MM-dd HH:mm:ss")
    private Date denyTime ;

    @ApiModelProperty(name = "mac_addr", value = "mac地址")
    @JSONField(name = "mac_addr")
    private String macAddr;

    @ApiModelProperty(name = "old_mac_addr", value = "变化之前的MAC地址")
    @JSONField(name = "old_mac_addr")
    private String oldMacAddr;

    @ApiModelProperty(name = "open_port", value = "开放端口")
    @JSONField(name = "open_port")
    private String openPort;

    @ApiModelProperty(name = "old_open_port", value = "变化之前的开放端口列表")
    @JSONField(name = "old_open_port")
    private String oldOpenPort;

    @ApiModelProperty(name = "model_des", value = "设备型号")
    @JSONField(name = "model_des")
    private String modelDes;

    @ApiModelProperty(name = "old_model_des", value = "变化之前的设备型号")
    @JSONField(name = "old_model_des")
    private String oldModelDes;

    @ApiModelProperty(name = "company", value = "当前公司名称")
    @JSONField(name = "company")
    private String company;

    @ApiModelProperty(name = "old_company", value = "公司名称")
    @JSONField(name = "old_company")
    private String oldCompany;

    public static DenyVO initDeny(AssetRespVO asset) {
        DenyVO denyVO = new DenyVO();
        denyVO.setMacAddr(asset.getCurMacAddr());
        denyVO.setOldMacAddr(asset.getMacAddr());

        denyVO.setOpenPort(asset.getCurOpenPort());
        denyVO.setOldOpenPort(asset.getOpenPort());

        denyVO.setCompany(asset.getCurCompany());
        denyVO.setOldCompany(asset.getCompany());

        denyVO.setModelDes(asset.getCurModelDes());
        denyVO.setOldModelDes(asset.getModelDes());
        return denyVO;
    }



}
