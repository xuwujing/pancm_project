package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "asset_branch_endpoint")
public class AssetBranchEndpoint implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 12位
     */
    private String pass;

    /**
     * device_branch表id
     */
    @Column(name = "asset_branch_id")
    private Integer assetBranchId;

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