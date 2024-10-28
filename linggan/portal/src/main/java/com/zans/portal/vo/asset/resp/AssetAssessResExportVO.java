package com.zans.portal.vo.asset.resp;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;
import com.zans.base.util.DateHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel
@Data
public class AssetAssessResExportVO {




    @ExcelProperty( value = "IP地址",index = 0 )
    private String ipAddr;

    @ExcelProperty( value = "点位名称",index = 1 )
    private String pointName;

    @ExcelProperty( value = "设备类型",index = 2 )
    private String deviceTypeName;

    @ExcelProperty( value = "运营商",index = 3 )
    private String operator;

    @ExcelProperty( value = "断光",index = 4 )
    private Integer afflineDuration;

    @ExcelProperty( value = "启用状态",index = 5 )
    private String enableStatusName;

    @ApiModelProperty(name = "aliveDuration", value = "在线时长")
    private Integer aliveDuration;

    @ApiModelProperty(name = "lastTime", value = "最近离线时长")
    private Integer lastTime;


    private Integer id;

    @ApiModelProperty(name = "areaId", value = "区域")
    private Integer areaId;

    @ApiModelProperty(name = "areaName", value = "大队名称")
    private String areaName;

    @ApiModelProperty(name = "region", value = "区域")
    private Integer region;

    @ApiModelProperty(name = "projectName", value = "项目名称")
    private String projectName;

    @ApiModelProperty(name = "regionName", value = "区域")
    private String regionName;



    @ApiModelProperty(name = "deviceType", value = "设备类型")
    private Integer deviceType;



    @ApiModelProperty(name = "brand", value = "品牌")
    private Integer brand;

    @ApiModelProperty(name = "brandName", value = "品牌")
    private String brandName;

    @ApiModelProperty(name = "model", value = "型号")
    private String model;



    @Deprecated
    @ApiModelProperty(name = "online", value = "在线状态 1 在线 0 离线")
    private Integer online;
    @ApiModelProperty(name = "alive", value = "在线状态 1:在线；2；不在线")
    private Integer alive;

    @ApiModelProperty(name = "status", value = "状态 '1启用 0 停用" )
    private Integer status;
    @ApiModelProperty(name = "enableStatus", value = "状态 '0停用；1启用" )
    private Integer enableStatus;

    @ApiModelProperty(name = "maintainStatus", value = "状态 '1启用 2 迁改停用 3.审核停用" )
    private Integer maintainStatus;

    @ApiModelProperty(name = "lon", value = "纬度")
    private BigDecimal lon;
    @ApiModelProperty(name = "lat", value = "经度")
    private BigDecimal lat;

    @ApiModelProperty(name = "acceptance", value = "是否验收")
    private Integer acceptance=0;//验收状态 1验收
    @ApiModelProperty(name = "acceptDate", value = "验收时间")
    private Date acceptDate;//验收时间
    @ApiModelProperty(name = "acceptIdea", value = "验收意见")
    private String acceptIdea;
    @ApiModelProperty(name = "consBatch", value = "建设批次1:一期,2:二期,3:新改扩")
    private Integer consBatch;//

    @ApiModelProperty(name = "offlineType",value = "在线状态 离线原因：0.断光,1掉电")
    private Integer offlineType;

    private String disposeUser;
    private String reason;
    @JSONField(name = "disposeTime", format = "yyyy-MM-dd HH:mm:ss")
    private Date disposeTime;

    @ApiModelProperty(name = "consBatchType", value = "建设批次分类:2(一期、二期),3:新改扩")
    private Integer consBatchType;


    @ApiModelProperty(name = "stateStatus", value = "申诉状态")
    private Integer stateStatus;


    @ApiModelProperty(name = "assessDuration", value = "考核时长")
    private Integer assessDuration;

    @ApiModelProperty(name = "offlineDuration", value = "断电时长")
    private Integer offlineDuration;




    @ApiModelProperty(name = "assessRate", value = "考核率")
    private Double assessRate;



    private String beginTime;
    private String endTime;





    public Integer getOnline(){
        if (alive != null && alive == 1){
            return 1;
        }else {
            return 0;
        }
    }
    public String getOnlineName(){
        if (alive != null && alive == 1){
            return "在线";
        }
        if (offlineType == null || offlineType == 0) {
            return "离线(断光)";
        } else {
            return "离线(掉电)";
        }
    }

    public Integer getStatus() {
        if (enableStatus != null && enableStatus == 1){
            return 0;
        }else {
            return 1;
        }
    }
    //0停用；1启用
    public String getStatusName(){
        if (enableStatus !=null && enableStatus == 1){
            return "启用";
        }
        return "停用";
    }

    public String getAcceptDate(){
        if (acceptDate != null){
            return DateHelper.formatDate(acceptDate,"yyyy-MM-dd");
        }
        return "";
    }

}
