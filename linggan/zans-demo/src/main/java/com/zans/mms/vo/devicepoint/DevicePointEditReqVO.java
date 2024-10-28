package com.zans.mms.vo.devicepoint;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel(value = "DevicePointEditReqVO", description = "")
@Data
public class DevicePointEditReqVO implements Serializable {

    /**
     * 点位ID
     */
    @NotBlank
    private Long pointId;

    /**
     * 点位编号
     */
    private String pointCode;

    /**
     * 点位名称
     */
    private String pointName;

    /**
     * 路口类；十字路口 丁字路口 人行通道
     */
    private String roadType;

    /**
     * 辖区ID
     */
    private String areaId;

    /**
     * 设备类型id
     */
    private String deviceType;

    /**
     * 取电方式，已装电表 未装电表
     */
    private String powerWay;

    /**
     * 电表位置 xxx方便的电表箱
     */
    private String powerPlace;

    /**
     * 通讯接入方式 VPN 、裸纤
     */
    private String networkLinkway;

    /**
     * GCJ02 经度
     */
    private String longitude;

    /**
     * GCJ02 维度
     */
    private String latitude;

    /**
     * 地图来源 1:百度；2:高德;3:腾讯;
     */
    private String mapSource;

    /**
     * 创建用户
     */
    private String creator;


    private static final long serialVersionUID = 1L;

}
