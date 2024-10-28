package com.zans.mms.vo.gear;

import com.zans.base.vo.BasePage;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qitian
 * @Title: zans-demo
 * @Description:一机一档筛选条件
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/6/29
 */
@Data
public class GearReqVO extends BasePage implements Serializable {

	/**
	 * 主键 自增
	 */
	private Long id;

	/**
	 * ip地址
	 */
	private String ip;

	/**
	 * mac地址
	 */
	private String mac;

	/**
	 * 设备类型
	 */
	private String deviceType;

	/**
	 * 设备型号
	 */
	private String deviceModel;


	/**
	 * 是否在线
	 */
	private Integer deviceStatus;

	/**
	 * 资产设备类型
	 */
	private String assetDeviceType;

	/**
	 * 是否在线
	 */
	private Integer alive;
}

