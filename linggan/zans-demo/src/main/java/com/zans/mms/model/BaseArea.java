package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
@Table(name = "base_area")
public class BaseArea implements Serializable {


    /**
     * 辖区ID
     */
    @Id
    @Column(name = "area_id")
    private String areaId;

    /**
     * 辖区名称
     */
    @Column(name = "area_name")
    private String areaName;

    /**
     * 武昌，汉口，汉阳
     */
    @Column(name = "area_type")
    private String areaType;

    /**
     * GCJ02 经度
     */
    private BigDecimal longitude;

    /**
     * GCJ02 维度
     */
    private BigDecimal latitude;

    /**
     * 禁用,启用
     */
    @Column(name = "enable_status")
    private String enableStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建用户
     */
    private String creator;

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


}
