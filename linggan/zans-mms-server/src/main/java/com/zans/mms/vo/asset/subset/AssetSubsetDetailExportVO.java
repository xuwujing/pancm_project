package com.zans.mms.vo.asset.subset;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

@ApiModel("资产子集导出excel模板实体")
@Data
@ToString
public class AssetSubsetDetailExportVO {

	/**
	 * 子集名称
	 */
	private String subsetName;

	/**
	 * 点位类型
	 */
	private String deviceType;

	/**
	 * 辖区
	 */
	private String areaName;

	/**
	 * 点位信息
	 */
	private String pointName;

	/**
	 * 方位
	 */
	private String deviceDirection;

	/**
	 * 点位编号
	 */
	private String pointCode;


	/**
	 * 设备编号
	 */
	private String assetCode;

	/**
	 * ip
	 */
	private String ip;


	/**
	 * 运维单位
	 */
	private String maintainCompany;

	/**
	 * 在线状态
	 */
	private String onlineStatus;

	/**
	 * 维护状态
	 */
	private String maintainStatus;

}
