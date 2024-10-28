package com.zans.portal.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "sys_region")
public class SysRegion implements Serializable {
    /**
     * 区域ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer regionId;

    /**
     * 上级ID
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 城市ID
     */
    @Column(name = "city_id")
    private Integer cityId;

    /**
     * 路由
     */
    @Column(name = "region_path")
    private String regionPath;

    /**
     * 区域名称
     */
    @Column(name = "region_name")
    private String regionName;

    private static final long serialVersionUID = 1L;

    /**
     * 获取区域ID
     *
     * @return region_id - 区域ID
     */
    public Integer getRegionId() {
        return regionId;
    }

    /**
     * 设置区域ID
     *
     * @param regionId 区域ID
     */
    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    /**
     * 获取上级ID
     *
     * @return parent_id - 上级ID
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 设置上级ID
     *
     * @param parentId 上级ID
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取城市ID
     *
     * @return city_id - 城市ID
     */
    public Integer getCityId() {
        return cityId;
    }

    /**
     * 设置城市ID
     *
     * @param cityId 城市ID
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * 获取路由
     *
     * @return region_path - 路由
     */
    public String getRegionPath() {
        return regionPath;
    }

    /**
     * 设置路由
     *
     * @param regionPath 路由
     */
    public void setRegionPath(String regionPath) {
        this.regionPath = regionPath == null ? null : regionPath.trim();
    }

    /**
     * 获取区域名称
     *
     * @return region_name - 区域名称
     */
    public String getRegionName() {
        return regionName;
    }

    /**
     * 设置区域名称
     *
     * @param regionName 区域名称
     */
    public void setRegionName(String regionName) {
        this.regionName = regionName == null ? null : regionName.trim();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", regionId=").append(regionId);
        sb.append(", parentId=").append(parentId);
        sb.append(", cityId=").append(cityId);
        sb.append(", regionPath=").append(regionPath);
        sb.append(", regionName=").append(regionName);
        sb.append("]");
        return sb.toString();
    }
}
