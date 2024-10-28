package com.zans.portal.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "map_catalogue")
public class MapCatalogue implements Serializable {
    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 上级id
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 1.目录；2.菜单
     */
    @Column(name = "department_type")
    private Integer departmentType;

    /**
     * 1,显示；2，隐藏
     */
    private Integer visible;

    /**
     * 图标名，icon等
     */
    private String icon;

    /**
     * 同级的顺序，小的靠前
     */
    private Integer seq;

    /**
     * 1,开启；2，禁用
     */
    private Integer enable;

    /**
     * 创建人
     */
    @Column(name = "create_id")
    private Integer createId;

    /**
     * 修改人
     */
    @Column(name = "update_id")
    private Integer updateId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取上级id
     *
     * @return parent_id - 上级id
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 设置上级id
     *
     * @param parentId 上级id
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取经度
     *
     * @return longitude - 经度
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 设置经度
     *
     * @param longitude 经度
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    /**
     * 获取纬度
     *
     * @return latitude - 纬度
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 设置纬度
     *
     * @param latitude 纬度
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    /**
     * 获取1.目录；2.菜单
     *
     * @return department_type - 1.目录；2.菜单
     */
    public Integer getDepartmentType() {
        return departmentType;
    }

    /**
     * 设置1.目录；2.菜单
     *
     * @param departmentType 1.目录；2.菜单
     */
    public void setDepartmentType(Integer departmentType) {
        this.departmentType = departmentType;
    }

    /**
     * 获取1,显示；2，隐藏
     *
     * @return visible - 1,显示；2，隐藏
     */
    public Integer getVisible() {
        return visible;
    }

    /**
     * 设置1,显示；2，隐藏
     *
     * @param visible 1,显示；2，隐藏
     */
    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    /**
     * 获取图标名，icon等
     *
     * @return icon - 图标名，icon等
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置图标名，icon等
     *
     * @param icon 图标名，icon等
     */
    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    /**
     * 获取同级的顺序，小的靠前
     *
     * @return seq - 同级的顺序，小的靠前
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * 设置同级的顺序，小的靠前
     *
     * @param seq 同级的顺序，小的靠前
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    /**
     * 获取1,开启；2，禁用
     *
     * @return enable - 1,开启；2，禁用
     */
    public Integer getEnable() {
        return enable;
    }

    /**
     * 设置1,开启；2，禁用
     *
     * @param enable 1,开启；2，禁用
     */
    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    /**
     * 获取创建人
     *
     * @return create_id - 创建人
     */
    public Integer getCreateId() {
        return createId;
    }

    /**
     * 设置创建人
     *
     * @param createId 创建人
     */
    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    /**
     * 获取修改人
     *
     * @return update_id - 修改人
     */
    public Integer getUpdateId() {
        return updateId;
    }

    /**
     * 设置修改人
     *
     * @param updateId 修改人
     */
    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", parentId=").append(parentId);
        sb.append(", name=").append(name);
        sb.append(", longitude=").append(longitude);
        sb.append(", latitude=").append(latitude);
        sb.append(", departmentType=").append(departmentType);
        sb.append(", visible=").append(visible);
        sb.append(", icon=").append(icon);
        sb.append(", seq=").append(seq);
        sb.append(", enable=").append(enable);
        sb.append(", createId=").append(createId);
        sb.append(", updateId=").append(updateId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}
