package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Data
@Table(name = "base_fault_type")
public class BaseFaultType implements Serializable {

    /**
     * 故障代码
     */
    @Id
    @Column(name = "fault_id")
    private String faultId;

    /**
     * 故障名称
     */
    @Column(name = "fault_name")
    private String faultName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序级别
     */
    private Integer sort;

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