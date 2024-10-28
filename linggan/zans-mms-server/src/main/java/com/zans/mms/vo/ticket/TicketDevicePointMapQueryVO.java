package com.zans.mms.vo.ticket;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:工单新建地图过滤实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/7/27
 */
@ApiModel(value = "DevicePointMapQueryVO", description = "")
@Data
public class TicketDevicePointMapQueryVO implements Serializable {



	/**
	 * 所属单位id
	 */
	private String orgId;

	/**
	 * 设备编号
	 */
	private String assetCode;

	/**
	 * 辖区
	 */
	private String areaId;

	/**
	 * 点位名称
	 */
	private String pointName;


	/**
	 * 点位名称和设备编号融合字段
	 */
	private String keyword;

	private String keyword1;

	/**
	 * 点位编号
	 */
	private String pointCode;

	/**
	 * 设备类型
	 */
	private String deviceType;

	/**
	 * 项目id
	 */
	private Integer projectId;
}
