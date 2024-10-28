package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "asset_branch")
public class AssetBranch implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    /**
     * 分组名
     */
    private String name;

    /**
     * 0,未删除；1，已删除
     */
    @Column(name = "delete_status")
    private Integer deleteStatus;

//    /**
//     * 0,未启用；1，已启用
//     */
//    @Column(name = "enable_stauts")
//    private Integer enableStauts;

    /**
     * 排序
     */
    @Column(name = "seq")
    private Integer seq;
    /**
     * 创建人
     */
    @Column(name = "creator_id")
    private Integer creatorId;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "level")
    private Integer level;

    @Column(name = "baseline_area_id")
    private Integer baselineAreaId;

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
