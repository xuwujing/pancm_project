package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Data
@Table(name = "base_device_type")
public class BaseDeviceType implements Serializable {

    /**
     * 设备类型id
     */
    @Id
    @Column(name = "type_id")
    private String typeId;

    /**
     * 设备类型名称
     */
    @Column(name = "type_name")
    private String typeName;

    /**
     * 设备类型别名
     */
    @Column(name = "type_alias")
    private String typeAlias;

    /**
     * 排序级别
     */
    private Integer sort;

    /**
     * 禁用,启用
     */
    @Column(name = "enable_status")
    private String enableStatus;

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
