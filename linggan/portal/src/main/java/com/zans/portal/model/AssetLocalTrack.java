package com.zans.portal.model;

import com.alibaba.fastjson.JSONObject;
import com.zans.base.vo.BasePage;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 资产轨迹表(AssetLocalTrack)实体类
 *
 * @author beixing
 * @since 2022-06-10 17:59:57
 */
@Data
@Table(name = "asset_local_track")
public class AssetLocalTrack extends BasePage implements Serializable {
    private static final long serialVersionUID = -99852298131385203L;
    @Column(name = "id")
    private Integer id;
    /**
     * 线路ID
     */
    @Column(name = "line_id")
    private String lineId;
    /**
     * 分组id
     */
    @Column(name = "group_id")
    private String groupId;
    /**
     * 分组名称
     */
    @Column(name = "group_name")
    private String groupName;
    /**
     * ip地址
     */
    @Column(name = "ip_addr")
    private String ipAddr;
    /**
     * 经度
     */
    @Column(name = "longitude")
    private Double longitude;
    /**
     * 维度
     */
    @Column(name = "latitude")
    private Double latitude;
    /**
     * 点位
     */
    @Column(name = "point_name")
    private String pointName;
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
