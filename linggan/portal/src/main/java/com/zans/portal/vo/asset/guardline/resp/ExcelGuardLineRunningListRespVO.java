package com.zans.portal.vo.asset.guardline.resp;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.zans.base.office.annotation.ExcelProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExcelGuardLineRunningListRespVO {

    @ApiModelProperty(name = "assetGuardLineName", value = "警卫线路名称")
    @ExcelProperty(value = "警卫线路名称", index = 1, width = 15)
    private String assetGuardLineName;

    @ApiModelProperty(name = "assetTotal", value = "设备总数")
    @ExcelProperty(value = "设备总数", index = 1, width = 15)
    private Integer assetTotal;

    @ApiModelProperty(name = "onlineNumber", value = "设备在线数")
    @ExcelProperty(value = "在线设备", index = 1, width = 15)
    private Integer onlineNumber;

    @ApiModelProperty(name = "offlineNumber", value = "设备离线数")
    @ExcelProperty(value = "离线设备", index = 1, width = 15)
    private Integer offlineNumber;

    @ApiModelProperty(name = "disableNumber", value = "设备停用数")
    @ExcelProperty(value = "停用设备", index = 1, width = 15)
    private Integer disableNumber;

    @ApiModelProperty(name = "onlineRate", value = "在线率")
    @ExcelProperty(value = "在线率", index = 1, width = 15)
    private String  onlineRate;

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
