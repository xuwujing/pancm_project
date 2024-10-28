package com.zans.portal.vo.switcher;

import com.zans.base.office.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
public class ExcelSwitchBranchVO {

    @ExcelProperty( value = "片区",index = 0, validate = {"not_empty"},isSelect = true)
    private String regionName;
    @ExcelProperty(value = "所属大队",index = 1, validate = {"not_empty"},isSelect = true)
    private String areaName;

    @ExcelProperty( value = "项目名称",index = 2, validate = {"not_empty"})
    private String projectName;

    @ApiModelProperty(name = "consBatch", value = "建设批次1 一期 2二期")
    @ExcelProperty( value = "建设批次",index = 3, validate = {"not_empty"})
    private String consBatch;//

    @ExcelProperty( value = "经度",index = 4, validate = {"not_empty"})
    private String  lon;
    @ExcelProperty( value = "纬度",index = 5, validate = {"not_empty"})
    private String lat;

    @ExcelProperty( value = "点位名称",index = 6, validate = {"not_empty"})
    private String pointName;

    @ExcelProperty(  value = "ip地址",index = 7, validate = {"not_empty"})
    private String ipAddr;

    @ExcelProperty( value = "交换机类型",index = 8, validate = {"not_empty"},isSelect = true)
    private String swTypeName;

    @ExcelProperty( value = "品牌",index = 9,isSelect = true )
    private String brand;
    @ExcelProperty( value = "型号",index = 10 )
    private String model;

    @ExcelProperty( value = "状态",index = 11, validate = {"not_empty"},isSelect = true)
    private String status;//0启用 1 停用

    @ExcelProperty( value = "验收状态",index = 12, isSelect = true)//状态 0未验收 1 已验收
    private String acceptStatus;

    @ExcelProperty(value = "验收时间",index = 13)
    private String acceptDate;

    @ExcelProperty( value = "交换机账户",index = 14)
    private String swAccount;
    @ExcelProperty( value = "交换机密码",index = 15)
    private String swPassword;

    @ExcelProperty(  value = "SNMP团体名",index = 16)
    private String community;

    @ExcelProperty(  value = "地图",index = 17)
    private String mapType;

    @ExcelProperty(value = "错误提示",index = 18)
    private String errMsg;




}
