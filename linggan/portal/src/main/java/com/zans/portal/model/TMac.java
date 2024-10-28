package com.zans.portal.model;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "t_mac")
public class TMac implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "mac_addr")
    private String macAddr;

    private String company;


    @JSONField(name = "create_time", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "create_time", value = "消息创建时间")
    private Date createTime;

    @JSONField(name = "update_time", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "update_time", value = "消息创建时间")
    private Date updateTime;

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

    /**
     * @return mac_addr
     */
    public String getMacAddr() {
        return macAddr;
    }

    /**
     * @param macAddr
     */
    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr == null ? null : macAddr.trim();
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

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
        sb.append(", macAddr=").append(macAddr);
        sb.append(", company=").append(company);
        sb.append("]");
        return sb.toString();
    }
}