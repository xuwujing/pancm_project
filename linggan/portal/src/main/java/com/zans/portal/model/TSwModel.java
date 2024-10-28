package com.zans.portal.model;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "t_sw_model")
public class TSwModel implements Serializable {
    /**
     * 交换机型号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 型号全称
     */
    @Column(name = "model_name")
    private String modelName;

    /**
     * 品牌: hw, h3c, cisco, ruijie
     */
    @Column(name = "model_brand")
    private String modelBrand;

    /**
     * 1,mac_out=arp_out; 2,mac_out<>arp_out
     */
    @Column(name = "sw_type")
    private Byte swType;

    /**
     * 交换机处理模块
     */
    @Column(name = "module_name")
    private String moduleName;

    /**
     * 交换机处理类
     */
    @Column(name = "class_name")
    private String className;

    /**
     * 具体型号
     */
    @Column(name = "model_desc")
    private String modelDesc;

    private static final long serialVersionUID = 1L;

    /**
     * 获取交换机型号
     *
     * @return id - 交换机型号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置交换机型号
     *
     * @param id 交换机型号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取型号全称
     *
     * @return model_name - 型号全称
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * 设置型号全称
     *
     * @param modelName 型号全称
     */
    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    /**
     * 获取品牌: hw, h3c, cisco, ruijie
     *
     * @return model_brand - 品牌: hw, h3c, cisco, ruijie
     */
    public String getModelBrand() {
        return modelBrand;
    }

    /**
     * 设置品牌: hw, h3c, cisco, ruijie
     *
     * @param modelBrand 品牌: hw, h3c, cisco, ruijie
     */
    public void setModelBrand(String modelBrand) {
        this.modelBrand = modelBrand == null ? null : modelBrand.trim();
    }

    /**
     * 获取1,mac_out=arp_out; 2,mac_out<>arp_out
     *
     * @return sw_type - 1,mac_out=arp_out; 2,mac_out<>arp_out
     */
    public Byte getSwType() {
        return swType;
    }

    /**
     * 设置1,mac_out=arp_out; 2,mac_out<>arp_out
     *
     * @param swType 1,mac_out=arp_out; 2,mac_out<>arp_out
     */
    public void setSwType(Byte swType) {
        this.swType = swType;
    }

    /**
     * 获取交换机处理模块
     *
     * @return module_name - 交换机处理模块
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * 设置交换机处理模块
     *
     * @param moduleName 交换机处理模块
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }

    /**
     * 获取交换机处理类
     *
     * @return class_name - 交换机处理类
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置交换机处理类
     *
     * @param className 交换机处理类
     */
    public void setClassName(String className) {
        this.className = className == null ? null : className.trim();
    }

    /**
     * 获取具体型号
     *
     * @return model_desc - 具体型号
     */
    public String getModelDesc() {
        return modelDesc;
    }

    /**
     * 设置具体型号
     *
     * @param modelDesc 具体型号
     */
    public void setModelDesc(String modelDesc) {
        this.modelDesc = modelDesc == null ? null : modelDesc.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", modelName=").append(modelName);
        sb.append(", modelBrand=").append(modelBrand);
        sb.append(", swType=").append(swType);
        sb.append(", moduleName=").append(moduleName);
        sb.append(", className=").append(className);
        sb.append(", modelDesc=").append(modelDesc);
        sb.append("]");
        return sb.toString();
    }
}