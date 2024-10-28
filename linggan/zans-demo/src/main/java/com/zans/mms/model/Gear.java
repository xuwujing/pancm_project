package com.zans.mms.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qitian
 * @Title: zans-demo
 * @Description:一机一档实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/29
 */
@Data
public class Gear implements Serializable {

	/**
	 * 主键 自增
	 */
	private Long id;

	/**
	 * 设备编码
	 */
	private String deviceId;

	/**
	 * 设备名称
	 */
	private String deviceName;

	/**
	 * 品牌
	 */
	private Integer brand;

	/**
	 * 行政区域
	 */
	private String areaId;

	private Integer monitorPointType;

	private String installAddress;

	private String longitude;

	private String latitude;

	private Integer deviceLocalType;

	private Integer networkType;

	private String areaPublicSecurity;

	private String installTime;

	private String manageUnit;

	private String manageUnitContact;

	private Integer videoSaveDay;

	private Integer deviceStatus;

	private Integer monitorPoint;

	private String deviceImgUrl;

	private String deviceModel;

	private String pointName;

	private String ipv4;

	private String ipv6;

	private String mac;

	private Integer deviceType;

	private Integer deviceFunctionType;

	private Integer lightProperty;

	private Integer cameraCodingFormat;

	private Integer department;

	private String industryCode;

	private Integer deviceCode;

	private Integer netId;

	private String creator;

	private String createTime;

	private Date updateTime;

	/**
	 * 是否在线
	 */
	private Integer alive;

	/**
	 * 资产设备类型
	 */
	private String assetDeviceType;

}
