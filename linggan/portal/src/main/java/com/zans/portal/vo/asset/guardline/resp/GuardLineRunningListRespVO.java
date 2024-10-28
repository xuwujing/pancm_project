package com.zans.portal.vo.asset.guardline.resp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class GuardLineRunningListRespVO {

    @ApiModelProperty(name = "assetGuardLineId", value = "警卫线路ID")
    private Integer assetGuardLineId;

    @ApiModelProperty(name = "assetGuardLineName", value = "警卫线路名称")
    private String assetGuardLineName;

    @ApiModelProperty(name = "assetTotal", value = "设备总数")
    private Integer assetTotal;

    @ApiModelProperty(name = "onlineNumber", value = "设备在线数")
    private Integer onlineNumber;

    @ApiModelProperty(name = "offlineNumber", value = "设备离线数")
    private Integer offlineNumber;

    @ApiModelProperty(name = "disableNumber", value = "设备停用数")
    private Integer disableNumber;

    @ApiModelProperty(name = "onlineRate", value = "在线率")
    private String  onlineRate;

    @ApiModelProperty(name = "creatorId", value = "创建人id")
    private Integer creatorId;

    @ApiModelProperty(name = "createName", value = "创建人")
    private String createName;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    @JSONField(name = "createTime", format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public void resetOnlineRate(){
        if (assetTotal == null || assetTotal<=0){
            onlineRate = "0%";
            return;
        }
        if (onlineNumber == null){
            onlineRate = "0%";
            return;
        }
        BigDecimal to = new BigDecimal(assetTotal.toString());
        BigDecimal on = new BigDecimal(onlineNumber.toString());
        onlineRate = on.multiply(new BigDecimal("100")).divide(to, 2, RoundingMode.HALF_UP).toPlainString()+"%";
    }

}
