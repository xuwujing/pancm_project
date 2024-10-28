package com.zans.portal.vo.radius;


import com.zans.base.office.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class EndPointViewExportVO {

    @ExcelProperty( value = "IP地址",index = 0 )
    private String ipAddr;

    @ExcelProperty( value = "点位名称",index = 1 )
    private String pointName;

    @ExcelProperty( value = "MAC地址",index = 2)
    private String mac;

    @ExcelProperty( value = "接入交换机",index = 3)
    private String nasIpAddress;

    @ExcelProperty( value = "在线状态",index = 4)
    private String aliveStatusName;

    @ExcelProperty( value = "档案类型",index = 5)
    private String deviceTypeName;

    @ExcelProperty( value = "识别类型",index = 6)
    private String deviceTypeNameDetect;

    @ExcelProperty( value = "识别方式",index = 7)
    private String detectTypeName;

    @ExcelProperty( value = "审核时间",index = 8)
    private String authTime;

    @ExcelProperty( value = "纳管设备",index = 9)
    private String assetManageName;




}
