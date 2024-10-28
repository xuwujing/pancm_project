package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
* @Title: DevicePointCheckLog
* @Description: 点位表校正日志表
* @Version:1.0.0
* @Since:jdk1.8
* @author beiming
* @date 4/12/21
*/
@Data
@Table(name = "device_point_check_log")
public class DevicePointCheckLog implements Serializable {
    /**
     * 自增id
     */
    @Id
    private Long id;

    /**
     * 点位id
     */
    @Column(name = "point_id")
    private Long pointId;

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

    /**
     * 修改前geo类型的经纬度
     */
    @Column(name = "prev_gis")
    private byte[] prevGis;

    /**
     * 修改后geo类型的经纬度
     */
    private byte[] gis;



    private static final long serialVersionUID = 1L;

}