package com.zans.portal.vo.asset.baseline;

import com.zans.base.office.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: 襄阳的实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-09-14 14:09:41
 */
@Data
public class AssetXYGABaselineExcelVO implements Serializable {


    /**
     * endpoint_profile放行时候的cur_ip_addr
     */
    @ExcelProperty(index = 1,value = "所属区域*")
    private String area;
    /**
     * 终端mac地址
     */
    @ExcelProperty(index = 2,value = "所属派出所*")
    private String areaNameLevel1;
    /**
     * endpoint_profile放行时候的cur_model_des
     */
    @ExcelProperty(index = 3,value = "点位名称*")
    private String pointName;
    /**
     * 品牌
     */
    @ExcelProperty(index = 4,value = "IP地址*")
    private String ipAddr;

    @ExcelProperty(index =5,value = "百度地图经度*")
    private String longitude;
    /**
     * endpoint_profile放行时候的cur_server_os
     */
    @ExcelProperty(index = 6,value = "百度地图纬度*")
    private String latitude;

    @ExcelProperty(index = 7,value = "设备品牌*")
    private String brandName;


    @ExcelProperty(index = 8,value = "设备型号*")
    private String modelDes;

    @ExcelProperty(index = 9,value = "设备类型*")
    private String deviceType;


    @ExcelProperty(index = 10,value = "项目名称")
    private String projectName;

    /**
     * 承建单位
     */
    @ExcelProperty(index = 11,value = "承建单位")
    private String contractor;

    /**
     * 承建单位联系人
     */
    @ExcelProperty(index = 11,value = "承建单位联系人")
    private String contractorPerson;
    /**
     * 承建单位联系电话
     */
    @ExcelProperty(index = 12,value = "承建单位联系电话")
    private String contractorPhone;
    /**
     * 维护单位
     */
    @ExcelProperty(index = 13,value = "维护单位")
    private String maintainCompany;
    /**
     * 维护联系人
     */
    @ExcelProperty(index = 14,value = "维护联系人")
    private String maintainPerson;
    /**
     * 维护联系电话
     */
    @ExcelProperty(index = 15,value = "维护联系电话")
    private String maintainPhone;


}
