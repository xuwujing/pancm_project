package com.zans.mms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Data
@Table(name = "base_vehicle")
public class BaseVehicle implements Serializable {


    /**
     * 车辆ID 车牌号
     */
    @Id
    @Column(name = "vehicle_id")
    private String vehicleId;

    /**
     * 车辆类型 工程车,吊车..
     */
    @Column(name = "vehicle_type")
    private String vehicleType;

    /**
     * 所属公司
     */
    private String company;

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
