package com.zans.portal.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "asset_blacklist")
public class AssetBlacklist implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * ip地址
     */
    @Column(name = "ip_addr")
    private String ipAddr;

    /**
     * 12位
     */
    private String pass;

    /**
     * 数据录入来源(0系统生成,1手工添加,2导入)
     */
    private Integer source;

    /**
     * 0,正常；1，删除
     */
    @Column(name = "delete_status")
    private Integer deleteStatus;

    /**
     * 录入人(创建人)
     */
    @Column(name = "create_person")
    private String createPerson;

    /**
     * 创建人
     */
    @Column(name = "creator_id")
    private Integer creatorId;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private Integer updateId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取ip地址
     *
     * @return ip_addr - ip地址
     */
    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * 设置ip地址
     *
     * @param ipAddr ip地址
     */
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr == null ? null : ipAddr.trim();
    }

    /**
     * 获取12位
     *
     * @return pass - 12位
     */
    public String getPass() {
        return pass;
    }

    /**
     * 设置12位
     *
     * @param pass 12位
     */
    public void setPass(String pass) {
        this.pass = pass == null ? null : pass.trim();
    }

    /**
     * 获取数据录入来源(0系统生成,1手工添加,2导入)
     *
     * @return source - 数据录入来源(0系统生成,1手工添加,2导入)
     */
    public Integer getSource() {
        return source;
    }

    /**
     * 设置数据录入来源(0系统生成,1手工添加,2导入)
     *
     * @param source 数据录入来源(0系统生成,1手工添加,2导入)
     */
    public void setSource(Integer source) {
        this.source = source;
    }

    /**
     * 获取0,正常；1，删除
     *
     * @return delete_status - 0,正常；1，删除
     */
    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * 设置0,正常；1，删除
     *
     * @param deleteStatus 0,正常；1，删除
     */
    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    /**
     * 获取录入人(创建人)
     *
     * @return create_person - 录入人(创建人)
     */
    public String getCreatePerson() {
        return createPerson;
    }

    /**
     * 设置录入人(创建人)
     *
     * @param createPerson 录入人(创建人)
     */
    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }

    /**
     * 获取创建人
     *
     * @return creator_id - 创建人
     */
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * 设置创建人
     *
     * @param creatorId 创建人
     */
    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * 获取更新人
     *
     * @return update_id - 更新人
     */
    public Integer getUpdateId() {
        return updateId;
    }

    /**
     * 设置更新人
     *
     * @param updateId 更新人
     */
    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
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
        sb.append(", ipAddr=").append(ipAddr);
        sb.append(", pass=").append(pass);
        sb.append(", source=").append(source);
        sb.append(", deleteStatus=").append(deleteStatus);
        sb.append(", createPerson=").append(createPerson);
        sb.append(", creatorId=").append(creatorId);
        sb.append(", updateId=").append(updateId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}