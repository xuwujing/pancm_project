package com.zans.portal.model;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "t_device_model_official")
public class TDeviceModelOfficial implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "brand_id")
    private Integer brandId;

    private String company;

    /**
     * 设备厂商编码
     */
    @Column(name = "model_code")
    private String modelCode;

    /**
     * 设备具体型号名称
     */
    @Column(name = "model_des")
    private String modelDes;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 设备型号id，t_device_type
     */
    @Column(name = "device_type_id")
    private Integer deviceTypeId;

    /**
     * 哑终端
     */
    @Column(name = "mute")
    private Integer mute;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    /**
     * @return company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company
     */
    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    /**
     * 获取设备厂商编码
     *
     * @return model_code - 设备厂商编码
     */
    public String getModelCode() {
        return modelCode;
    }

    /**
     * 设置设备厂商编码
     *
     * @param modelCode 设备厂商编码
     */
    public void setModelCode(String modelCode) {
        this.modelCode = modelCode == null ? null : modelCode.trim();
    }

    /**
     * 获取设备具体型号名称
     *
     * @return model_des - 设备具体型号名称
     */
    public String getModelDes() {
        return modelDes;
    }

    /**
     * 设置设备具体型号名称
     *
     * @param modelDes 设备具体型号名称
     */
    public void setModelDes(String modelDes) {
        this.modelDes = modelDes == null ? null : modelDes.trim();
    }

    /**
     * 获取备注信息
     *
     * @return remark - 备注信息
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注信息
     *
     * @param remark 备注信息
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取设备型号id，t_device_type
     *
     * @return device_type_id - 设备型号id，t_device_type
     */
    public Integer getDeviceTypeId() {
        return deviceTypeId;
    }

    /**
     * 设置设备型号id，t_device_type
     *
     * @param deviceTypeId 设备型号id，t_device_type
     */
    public void setDeviceTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public Integer getMute() {
        return mute;
    }

    public void setMute(Integer mute) {
        this.mute = mute;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", company=").append(company);
        sb.append(", modelCode=").append(modelCode);
        sb.append(", modelDes=").append(modelDes);
        sb.append(", remark=").append(remark);
        sb.append(", deviceTypeId=").append(deviceTypeId);
        sb.append(", mute=").append(mute);
        sb.append("]");
        return sb.toString();
    }
}