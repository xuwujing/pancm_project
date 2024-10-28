package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Data
@Table(name = "asset_subset_detail")
public class AssetSubsetDetail implements Serializable {
    /**
     * 自增id
     */
    @Id
    private Long id;

    /**
     * 资产子集ID
     */
    @Column(name = "subset_id")
    private Long subsetId;

    /**
     * 资产ID
     */
    @Column(name = "asset_id")
    private Long assetId;

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
