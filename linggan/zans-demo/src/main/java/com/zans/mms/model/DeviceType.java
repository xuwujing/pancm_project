package com.zans.mms.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "t_device_type")
public class DeviceType implements Serializable {
    /**
     * 设备类别编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typeId;

    /**
     * 设备类别
     */
    @Column(name = "type_name")
    private String typeName;

    /**
     * 是否哑终端 0：否 1：是
     */
    @Column(name = "mute")
    private Integer mute;

    /**
     * 模板地址，相对路径
     */
    private String template;

    private static final long serialVersionUID = 1L;

    /**
     * 获取设备类别编号
     *
     * @return type_id - 设备类别编号
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * 设置设备类别编号
     *
     * @param typeId 设备类别编号
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * 获取设备类别
     *
     * @return type_name - 设备类别
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * 设置设备类别
     *
     * @param typeName 设备类别
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    /**
     * 获取是否哑终端 0：否 1：是
     *
     * @return is_mute - 是否哑终端 0：否 1：是
     */
    public Integer getMute() {
        return mute;
    }

    /**
     * 设置是否哑终端 0：否 1：是
     *
     * @param mute 是否哑终端 0：否 1：是
     */
    public void setMute(Integer mute) {
        this.mute = mute;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", typeId=").append(typeId);
        sb.append(", typeName=").append(typeName);
        sb.append(", mute=").append(mute);
        sb.append(", template=").append(template);
        sb.append("]");
        return sb.toString();
    }
}