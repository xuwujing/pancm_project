package com.zans.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author beixing
 * @Title: 设备型号和类型对应表(DeviceFeatureModelOfficial)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-07-28 18:14:06
 */
@Data
@Table(name = "device_feature_model_official")
public class DeviceFeatureModelOfficial implements Serializable {
    private static final long serialVersionUID = 424726190376965424L;
    @Column(name = "id")
    private Integer id;
    /**
     * 设备厂商编码
     */
    @Column(name = "model_code")
    private String modelCode;
    /**
     * 更新之后的编码
     */
    @Column(name = "model_code_new")
    private String modelCodeNew;
    /**
     * 设备型号id，t_device_type
     */
    @Column(name = "device_type_id")
    private Integer deviceTypeId;
    /**
     * 设备类型名称
     */
    @Column(name = "device_type_name")
    private String deviceTypeName;
    /**
     * 根据型号分析的类型
     */
    @Column(name = "device_type_new")
    private String deviceTypeNew;
    /**
     * 品牌id
     */
    @Column(name = "brand_id")
    private Integer brandId;
    /**
     * 品牌名称
     */
    @Column(name = "brand_name")
    private String brandName;
    /**
     * 公司，sys_brand.company
     */
    @Column(name = "company")
    private String company;
    /**
     * 设备具体型号名称
     */
    @Column(name = "model_des")
    private String modelDes;
    /**
     * 备注信息
     */
    @Column(name = "remark")
    private String remark;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
