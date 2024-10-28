package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "asset_branch_asset")
public class AssetBranchAsset implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    /**
     * device_branch表id
     */
    @Column(name = "asset_branch_id")
    private Integer assetBranchId;


    @Column(name = "ip_addr")
    private String ipAddr;

    /**
     * 0,未删除；1，已删除
     */
    @Column(name = "delete_status")
    private Integer deleteStatus;

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

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
