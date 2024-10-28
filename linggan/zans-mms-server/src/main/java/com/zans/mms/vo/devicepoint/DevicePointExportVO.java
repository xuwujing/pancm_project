package com.zans.mms.vo.devicepoint;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author qitian
 * @Title: zans-mms-server
 * @Description:点位导出实体
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2021/5/18
 */
@ApiModel(value = "DevicePointExportVO", description = "点位导出实体")
@Data
public class DevicePointExportVO {


	private Long id;

	private String pointCode;

	private String pointName;

	private String areaName;

	private String roadType;

	private String deviceType;

	private String powerWay;

	private String networkLinkway;

	private String longitude;

	private String latitude;

	private String orgName;
}
