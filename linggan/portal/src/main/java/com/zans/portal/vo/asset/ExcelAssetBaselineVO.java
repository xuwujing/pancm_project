package com.zans.portal.vo.asset;

import com.zans.base.office.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author beixing
 * @Title: 基线表(AssetBaseline)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-08 14:09:41
 */
@Data
public class ExcelAssetBaselineVO implements Serializable {

    /**
     * endpoint_profile放行时候的cur_ip_addr
     */
    @ExcelProperty(index = 1,value = "ipAddr")
    private String ipAddr;
    /**
     * 终端mac地址
     */
    @ExcelProperty(index = 2,value = "mac地址")
    private String mac;
    /**
     * endpoint_profile放行时候的cur_model_des
     */
    @ExcelProperty(index = 3,value = "curModelDes")
    private String modelDes;
    /**
     * 品牌
     */
    @ExcelProperty(index = 4,value = "品牌")
    private String brandName;
    /**
     * endpoint_profile放行时候的cur_device_type
     */
//    @ExcelProperty(index =5,value = "当前设备类型")
    private Integer deviceType;
    @ExcelProperty(index =5,value = "当前设备类型")
    private String deviceTypeName;
    /**
     * endpoint_profile放行时候的cur_server_os
     */
    @ExcelProperty(index = 6,value = "curServerOs")
    private String serverOs;

    @ExcelProperty(index = 7,value = "开放端口")
    private String open_port;
    /**
     * nas_ip
     */
    @ExcelProperty(index = 8,value = "nasIp")
    private String nasIp;
    /**
     * nas_port_id
     */
    @ExcelProperty(index = 9,value = "nasPortId")
    private String nasPortId;
    /**
     * 虚拟局域网网络
     */
    @ExcelProperty(index = 10,value = "虚拟局域网网络")
    private String vlan;
    /**
     * 点位名称
     */
    @ExcelProperty(index = 11,value = "线路名称")
    private String pointName;
    /**
     * 经度
     */
    @ExcelProperty(index = 12,value = "经度")
    private Double longitude;
    /**
     * 纬度
     */
    @ExcelProperty(index = 13,value = "纬度")
    private Double latitude;
    /**
     * 项目名称
     */
    @ExcelProperty(index = 14,value = "项目名称")
    private String projectName;
    /**
     * 承建单位
     */
    @ExcelProperty(index = 15,value = "承建单位")
    private String contractor;
    /**
     * 项目状态：0建设中，1质保，2过保，3维保
     */
//    @ExcelProperty(index = 16,value = "项目状态")
    private Integer projectStatus;

    @ExcelProperty(index = 16,value = "项目状态")
    private String projectStatusName;
    /**
     * 承建单位联系人
     */
    @ExcelProperty(index = 17,value = "承建单位联系人")
    private String contractorPerson;
    /**
     * 承建单位联系电话
     */
    @ExcelProperty(index = 18,value = "承建单位联系电话")
    private String contractorPhone;
    /**
     * 维护单位
     */
    @ExcelProperty(index = 19,value = "维护单位")
    private String maintainCompany;
    /**
     * 维护联系人
     */
    @ExcelProperty(index = 20,value = "维护联系人")
    private String maintainPerson;
    /**
     * 维护联系电话
     */
    @ExcelProperty(index = 21,value = "维护联系电话")
    private String maintainPhone;
    /**
     * map_catalogue.id
     */
    @ExcelProperty(index = 22,value = "mapCatalogueId")
    private Integer mapCatalogueId;
    /**
     * 0:ip、mac未绑定，mac为空;1:已绑定
     */
//    @ExcelProperty(index = 23,value = "ip/mac是否绑定")
    private Integer bindStatus;
    @ExcelProperty(index = 23,value = "ip/mac是否绑定")
    private String bindStatusName;
    /**
     * 审批意见
     */
    @ExcelProperty(index = 24,value = "审批意见")
    private String remark;

}
