package com.zans.portal.vo.switcher;

import com.zans.base.office.annotation.ExcelProperty;
import lombok.Data;


@Data
public class ExcelSwitchBranchExportVO {

    @ExcelProperty( value = "片区",index = 0, validate = {"not_empty"} )
    private String regionName;
    @ExcelProperty(value = "所属大队",index = 1, validate = {"not_empty"} )
    private String areaName;

    @ExcelProperty( value = "项目名称",index = 2, validate = {"not_empty"})
    private String projectName;

    @ExcelProperty( value = "经度",index = 3, validate = {"not_empty"})
    private String  lon;
    @ExcelProperty( value = "纬度",index = 4, validate = {"not_empty"})
    private String lat;



    @ExcelProperty( value = "点位名称",index = 5, validate = {"not_empty"})
    private String pointName;

    @ExcelProperty(  value = "ip地址",index = 6, validate = {"not_empty"})
    private String ipAddr;

    @ExcelProperty( value = "交换机类型",index = 7, validate = {"not_empty"} )
    private String swTypeName;

    @ExcelProperty( value = "品牌",index = 8)
    private String brandName;
    @ExcelProperty(value = "型号",index = 9)
    private String model;

    @ExcelProperty(value = "验收状态",index = 10)
    private String acceptance;//验收状态 1验收
    @ExcelProperty(  value = "验收时间",index = 11)
    private String acceptDate;//验收时间

    @ExcelProperty(  value = "下行设备数",index = 12)
    private Integer scanMacAll;

    //2/8 已用端口/总数物理
    @ExcelProperty(  value = "已用端口",index = 13)
    private String useInterface;

    @ExcelProperty( value = "状态",index = 14  )
    private String statusName;


    @ExcelProperty( value = "在线状态",index = 15  )
    private String offlineName;



}
