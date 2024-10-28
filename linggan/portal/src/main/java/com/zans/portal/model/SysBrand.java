package com.zans.portal.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "device_brand")
public class SysBrand implements Serializable {
    @Id
    @Column(name = "brand_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer brandId;

    /**
     * 品牌名称
     */
    @Column(name = "brand_name")
    private String brandName;

    /**
     * 公司名称
     */
    private String company;

    /**
     * like 查询表达式
     */
    @Column(name = "brand_desc")
    private String brandDesc;
    /**
     * 是否允许删除，
     * 0:可以删除，1:不可以删除
     */
    @Column(name = "confirm")
    private Integer confirm;

    private static final long serialVersionUID = 1L;

    /**
     * @return brand_id
     */
    public Integer getBrandId() {
        return brandId;
    }

    /**
     * @param brandId
     */
    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getConfirm() {
        return confirm;
    }

    public void setConfirm(Integer confirm) {
        this.confirm = confirm;
    }

    /**
     * 获取品牌名称
     *
     * @return brand_name - 品牌名称
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * 设置品牌名称
     *
     * @param brandName 品牌名称
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    /**
     * 获取公司名称
     *
     * @return company - 公司名称
     */
    public String getCompany() {
        return company;
    }

    /**
     * 设置公司名称
     *
     * @param company 公司名称
     */
    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    /**
     * 获取like 查询表达式
     *
     * @return brand_desc - like 查询表达式
     */
    public String getBrandDesc() {
        return brandDesc;
    }

    /**
     * 设置like 查询表达式
     *
     * @param brandDesc like 查询表达式
     */
    public void setBrandDesc(String brandDesc) {
        this.brandDesc = brandDesc == null ? null : brandDesc.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", brandId=").append(brandId);
        sb.append(", brandName=").append(brandName);
        sb.append(", company=").append(company);
        sb.append(", confirm=").append(confirm);
        sb.append(", brandDesc=").append(brandDesc);
        sb.append("]");
        return sb.toString();
    }
}
