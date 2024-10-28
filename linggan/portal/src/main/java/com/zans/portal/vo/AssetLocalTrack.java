package com.zans.portal.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 资产轨迹表(AssetLocalTrack)实体类
 *
 * @author beixing
 * @since 2022-06-10 17:59:56
 */
@Data
public class AssetLocalTrack implements Serializable {
    private static final long serialVersionUID = -34451209788195563L;
    private Integer id;
    /**
     * 线路ID
     */
    private String lineId;
    /**
     * 分组id
     */
    private String groupId;
    /**
     * 分组名称
     */
    private String groupName;
    /**
     * ip地址
     */
    private String ipAddr;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 维度
     */
    private Double latitude;
    /**
     * 点位
     */
    private String pointName;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
