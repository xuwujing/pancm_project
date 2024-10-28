package com.zans.portal.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author beixing
 * @Title: (AssetBaselineArea)实体类
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021-09-14 17:04:11
 */
@Data
@Table(name = "asset_baseline_area")
public class AssetBaselineArea implements Serializable {
    private static final long serialVersionUID = -86488889489726664L;
    @Column(name = "id")
    private Integer id;
    /**
     * 父级节点
     */
    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "level")
    private Integer level;
    /**
     * 辖区名称
     */
    @Column(name = "area_name")
    private String areaName;
    /**
     * ip地址起
     */
    @Column(name = "ip_seg_begin")
    private String ipSegBegin;
    /**
     * ip地址止
     */
    @Column(name = "ip_seg_end")
    private String ipSegEnd;
    /**
     * vlan
     */
    @Column(name = "vlan")
    private String vlan;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
