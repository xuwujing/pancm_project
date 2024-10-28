package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Data
@Table(name = "asset_subset")
public class AssetSubset implements Serializable {
    /**
     * 自增id
     */
    @Id
    private Long id;

    /**
     * 子集名称
     */
    @Column(name = "subset_name")
    private String subsetName;


    @Column(name = "device_type")
    private String deviceType;

    /**
     * 排序级别
     */
    private Integer sort;

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
