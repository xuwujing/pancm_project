package com.zans.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author beixing
 * @Title: 设备型号和类型对应表(DeviceFeatureModelOfficial)请求响应对象
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-28 18:14:05
 */
@Data
public class DeviceFeatureModelOfficialVO implements Serializable {
    private static final long serialVersionUID = 284367716108160222L;
    private Integer id;
    /**
     * 设备厂商编码
     */
    private String modelCode;
    /**
     * 更新之后的编码
     */
    private String modelCodeNew;
    /**
     * 设备型号id，t_device_type
     */
    private Integer deviceTypeId;
    /**
     * 设备类型名称
     */
    private String deviceTypeName;
    /**
     * 根据型号分析的类型
     */
    private String deviceTypeNew;
    /**
     * 品牌id
     */
    private Integer brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 公司，sys_brand.company
     */
    private String company;
    /**
     * 设备具体型号名称
     */
    private String modelDes;
    /**
     * 备注信息
     */
    private String remark;
    private Date createTime;
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
