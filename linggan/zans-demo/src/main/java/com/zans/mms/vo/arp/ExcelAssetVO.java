package com.zans.mms.vo.arp;

import com.zans.base.office.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author xv
 * @since 2020/4/30 10:42
 */
@Data
public class ExcelAssetVO {
    
    @ExcelProperty(ignore = true)
    private Integer id;

    @ExcelProperty(value = "IP地址", index = 1, width = 15)
    private String ip;

    @ExcelProperty(ignore = true)
    private Integer areaId;

    @ExcelProperty(value = "区域", index = 2)
    private String areaName;

    @ExcelProperty(ignore = true)
    private Integer deviceType;

    @ExcelProperty(value = "设备类型", index = 5)
    private String deviceTypeName;

    /**
     * alive = 0 | 1, 不设置
     * alive = 2， 设置红色
     */
    @ExcelProperty(ignore = true)
    private Integer alive;

    @ExcelProperty(value = "在线", index = 6, colors = { "在线", "", "离线", "red"})
    private String aliveName;

    @ExcelProperty(ignore = true)
    private Integer enableStatus;

    @ExcelProperty(value = "状态", index = 7, colors = { "启用", "", "禁用", "red"})
    private String enableStatusName;

    @ExcelProperty(ignore = true)
    private Integer source;

    @ExcelProperty(value = "来源", index = 8)
    private String sourceName;


    @ExcelProperty(ignore = true)
    private Integer mute;

    @ExcelProperty(value = "哑终端", index = 9)
    private String muteName;

    @ExcelProperty(ignore = true)
    private Integer disStatus;

//    @ExcelProperty(value = "分配状态", index = 10)
//    private String disStatusName;

    @ExcelProperty(ignore = true)
    private Integer ipStatus;

    @ExcelProperty(ignore = true)
    private String ipStatusName;

    @ExcelProperty(value = "最近在线时间", index = 10, dateFormat="yyyy-MM-dd HH:mm:ss", type="Date", width = 20)
    private Date aliveLastTime;

    @ExcelProperty(value = "设备公司", index = 11, width = 30)
    private String deviceCompany;

    @ExcelProperty(value = "设备型号", index = 12, width = 25)
    private String modelDes;

    @ExcelProperty(value = "项目名称", index = 20, width = 25)
    private String projectName;

    @ExcelProperty(value = "点位名称", index = 21, width = 25)
    private String pointName;

    @ExcelProperty(ignore = true)
    private Integer projectStatus;

    @ExcelProperty(value = "项目状态", index = 22, width = 25)
    private String projectStatusName;

    @ExcelProperty(value = "承建单位", index = 23, width = 25)
    private String contractor;

    @ExcelProperty(value = "承建联系人", index = 24)
    private String contractorPerson;

    @ExcelProperty(value = "承建电话", index = 25)
    private String contractorPhone;

    @ExcelProperty(value = "维护单位", index = 35)
    private String maintainCompany;

    @ExcelProperty(value = "维护联系人", index = 36)
    private String maintainPerson;

    @ExcelProperty(value = "维护电话", index = 37)
    private String maintainPhone;

    @ExcelProperty(ignore = true)
    private Integer maintainStatus;

    @ExcelProperty(value = "维护状态", index = 38, width = 25)
    private String maintainStatusName;

//    @ExcelProperty(value = "项目设备品牌", index = 41, width = 25)
//    private String deviceModelBrand;
//
//    @ExcelProperty(value = "项目设备类型", index = 42, width = 25)
//    private String deviceModelType;
//
//    @ExcelProperty(value = "项目设备型号", index = 43, width = 25)
//    private String deviceModelDes;

    public void setAliveByMap(Map<Object, String> map) {
        Integer status = this.getAlive();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setAliveName(name);
    }

    public void setDisStatusByMap(Map<Object, String> map) {
        Integer status = this.getDisStatus();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
//        this.setDisStatusName(name);
    }

    public void setMuteByMap(Map<Object, String> map) {
        Integer status = this.getMute();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setMuteName(name);
    }

    public void setIpStatusByMap(Map<Object, String> map) {
        Integer status = this.getIpStatus();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setIpStatusName(name);
    }

    public void setProjectStatusByMap(Map<Object, String> map) {
        Integer status = this.getProjectStatus();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setProjectStatusName(name);
    }

     /**
     * 设置资产状态所对应的名称
     * @param map
     */
    public void setEnableStatusNameByMap(Map<Object, String> map){
        Integer status = this.enableStatus;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setEnableStatusName(name);
    }

    /**
     * 设置资产录入来源所对应的名称
     * @param map
     */
    public void setSourceNameByMap(Map<Object, String> map){
        Integer status = this.source;
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        if (status.intValue() == 1 || status.intValue() == 2 || status.intValue() == 3){
            status = 0;
        }else if (status.intValue() == 4){
            status = 1;
        }else if (status.intValue() == 5){
            status = 2;
        }else{
            status = -1;
        }
        String name = map.get(status);
        this.setSourceName(name);
    }

    public void setMaintainStatusNameByMap(Map<Object, String> map) {
        Integer status = this.getMaintainStatus();
        if (status == null || map == null || map.size() == 0) {
            return;
        }
        String name = map.get(status);
        this.setMaintainStatusName(name);
    }
}
