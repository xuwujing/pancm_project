package com.zans.portal.vo.ip;

import com.alibaba.fastjson.annotation.JSONField;
import com.zans.base.office.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author xv
 * @since 2020/3/20 19:05
 */
@Data
public class ExcelIpAssignVO {

    @ExcelProperty(value = {"序号", "ID"}, type = "integer", validate = {"not_empty"})
    private Integer seq;

    @ExcelProperty(value = {"点位名称", "设备名称"}, validate = {"not_empty"})
    @JSONField(name = "point_name")
    private String pointName;

    private String area;

    @ExcelProperty(value = {"所属片区", "所在辖区", "所属辖区", "三级单位"})
    @JSONField(name = "area_name")
    private String areaName;

    @ExcelProperty(value = {"IP", "IP地址"})
    @JSONField(name = "ip_addr")
    private String ipAddr;

    @ExcelProperty(value = {"MAC地址", "mac地址"})
    @JSONField(name = "mac_addr")
    private String macAddr;

    @ExcelProperty(value = "网关")
    private String gateway;

    @ExcelProperty(value = "掩码")
    private String mask;

    @ExcelProperty(value = {"vlan号", "vlan", "VLAN", "VLAN号"}, type = "integer")
    private Integer vlan;

    @ExcelProperty(value = {"监控所属工程项目", "%所属工程项目", "采购项目"})
    @JSONField(name = "project_name")
    private String projectName;

    @ExcelProperty(value = {"建设单位", "采购单位"})
    private String company;

    @ExcelProperty(value = {"承建单位", "安装单位"})
    private String contractor;

    @ExcelProperty(value = {"摄像机品牌", "品牌"})
    @JSONField(name = "device_model_brand")
    private String deviceModelBrand;

    @ExcelProperty(value = {"摄像机型号", "型号"})
    @JSONField(name = "device_model_des")
    private String deviceModelDes;

    private Integer rowIndex;
}
